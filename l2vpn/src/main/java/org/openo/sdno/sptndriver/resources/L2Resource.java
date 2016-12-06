/*
 * Copyright 2016 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.sptndriver.resources;

import com.codahale.metrics.annotation.Timed;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.openo.sdno.sptndriver.converter.L2Converter;
import org.openo.sdno.sptndriver.converter.SRouteCalReqsInitiator;
import org.openo.sdno.sptndriver.db.dao.UuidMapDao;
import org.openo.sdno.sptndriver.db.model.UuidMap;
import org.openo.sdno.sptndriver.exception.ResourceNotFoundException;
import org.openo.sdno.sptndriver.exception.ServerException;
import org.openo.sdno.sptndriver.models.north.NCreateL2vpnReq;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SDeleteEline;
import org.openo.sdno.sptndriver.models.south.SDeleteElineInput;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.openo.sdno.sptndriver.services.ElineService;
import org.openo.sdno.sptndriver.services.TunnelService;
import org.openo.sdno.sptndriver.utils.EsrUtil;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.skife.jdbi.v2.DBI;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The class to provide L2vpn resource.
 */
@Path("/openoapi/sbi-l2vpn-vpws/v1/")
@Produces(MediaType.APPLICATION_JSON)
@Api(tags = {"L2vpn API"})
public class L2Resource {

  private static final org.slf4j.Logger LOGGER =
      LoggerFactory.getLogger(L2Resource.class);

  private final UuidMapDao uuidMapDao;

  /**
   * The constructor.
   *
   * @param jdbi Database parameter
   */
  public L2Resource(DBI jdbi) {
    this.uuidMapDao = jdbi.onDemand(UuidMapDao.class);
  }

  /**
   * The post method to create L2vpn.
   *
   * @param createL2vpnReq Parameter of create L2vpn.
   * @return 201 if success
   */
  @POST
  @Path("/l2vpn_vpwss")
  @ApiOperation(value = "Create a L2vpn ",
      code = HttpStatus.CREATED_201,
      response = NCreateL2vpnReq.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.BAD_REQUEST_400,
          message = "Create L2Vpn Instances failure as parameters invalid.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNAUTHORIZED_401,
          message = "Unauthorized",
          response = String.class),
      @ApiResponse(code = HttpStatus.NOT_FOUND_404,
          message = "Create L2Vpn Instances failure as can't reach server.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
          message = "Unprocessable L2vpn Entity.",
          response = String.class),
      @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
          message = "Create L2Vpn Instances failure as inner error.",
          response = String.class)
      })
  @Produces(MediaType.APPLICATION_JSON)
  @Timed
  public Response createL2vpn(@ApiParam(value = "L2vpn information",
      required = true) NCreateL2vpnReq createL2vpnReq,
                              @ApiParam(value = "Controller uuid, "
                                  + "the format is X-Driver-Parameter:extSysID={ctrlUuid}",
                                  required = true)
                              @HeaderParam("X-Driver-Parameter") String controllerIdPara)
      throws URISyntaxException {
    LOGGER.info("Create l2vpn begin.");
    NL2Vpn l2vpn = createL2vpnReq.getL2vpnVpws();
    if (l2vpn.getId() == null || l2vpn.getId().isEmpty()) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("L2vpn id is null or empty.")
          .build();
    }
    String externalId;
    String controllerId;
    try {
      controllerId = ServiceUtil.getControllerId(controllerIdPara);
      SRouteCalReqsInput routeCalInput = SRouteCalReqsInitiator.initElineLspCalRoute(l2vpn);
      String controllerUrl = EsrUtil.getSdnoControllerUrl(controllerId);
      TunnelService tunnelServices = new TunnelService(controllerUrl);
      SCreateElineAndTunnelsInput createElineAndTunnels =
          L2Converter.convertL2ToElineTunnerCreator(l2vpn);

      // Calculate LSP route first.
      createElineAndTunnels.getInput().setRouteCalResults(
          tunnelServices.calcRoutes(routeCalInput).getOutput().getRouteCalResults());
      // Create Eline.
      ElineService elineServices = new ElineService(controllerUrl);
      externalId = elineServices.createElineAndTunnels(createElineAndTunnels);
    } catch (ServerException ex) {
      LOGGER.error("Create l2vpn failed, due to: "
          + ExceptionUtils.getStackTrace(ex));
      return ex.getResponse();
    }


    uuidMapDao.insert(l2vpn.getId(), externalId, UuidMap.UuidTypeEnum.ELINE.name(), controllerId);
    LOGGER.info("Create l2vpn end.");
    return Response.status(Response.Status.CREATED)
        .entity(l2vpn).build();
  }

  /**
   * The delete method to delete L2vpn.
   *
   * @param vpnid Global UUID of L2vpn(UUID in SDN-O).
   * @return 200 if success
   */
  @DELETE
  @Path("/l2vpn_vpwss/{vpnid}")
  @ApiOperation(value = "Delete a L2Vpn Connection ",
      code = HttpStatus.OK_200,
      response = NL2Vpn.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.BAD_REQUEST_400,
          message = "Delete a L2Vpn Instance failure as parameters invalid.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNAUTHORIZED_401,
          message = "Unauthorized",
          response = String.class),
      @ApiResponse(code = HttpStatus.NOT_FOUND_404,
          message = "Delete a L2Vpn Instances failure as can't reach server.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
          message = "Unprocessable L2vpn Entity.",
          response = String.class),
      @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
          message = "Delete a L2Vpn Instances failure as inner error.",
          response = String.class)
      })
  @Produces(MediaType.APPLICATION_JSON)
  @Timed
  public Response deleteL2vpn(@ApiParam(value = "L2vpn uuid", required = true)
                              @PathParam("vpnid") @NotNull String vpnid,
                              @ApiParam(value = "Controller uuid, "
                                  + "the format is X-Driver-Parameter:extSysID={ctrlUuid}",
                                  required = true)
                              @HeaderParam("X-Driver-Parameter") String controllerIdPara)
      throws URISyntaxException {
    LOGGER.info("Delete l2vpn begin, id is: " + vpnid);
    String controllerId;
    String southElineId;
    NL2Vpn l2vpn = new NL2Vpn();
    l2vpn.setId(vpnid);
    try {
      controllerId = ServiceUtil.getControllerId(controllerIdPara);
      southElineId = getSouthElineId(vpnid, controllerId);

      SDeleteElineInput elineDeleteInput = new SDeleteElineInput();
      SDeleteEline deleteEline = new SDeleteEline();
      deleteEline.setElineId(southElineId);
      elineDeleteInput.setInput(deleteEline);
      ElineService elineServices = new ElineService(
          EsrUtil.getSdnoControllerUrl(controllerId));
      elineServices.deleteEline(elineDeleteInput);
    } catch (ServerException ex) {
      LOGGER.error("Delete l2vpn failed, due to: "
          + ExceptionUtils.getStackTrace(ex));
      return ex.getResponse();
    } catch (ResourceNotFoundException ex) {
      LOGGER.warn("Delete l2vpn successful, though it is not found in driver: "
          + ExceptionUtils.getStackTrace(ex));
      return Response.status(Response.Status.OK)
          .entity(l2vpn).build();
    }
    uuidMapDao.delete(vpnid, UuidMap.UuidTypeEnum.ELINE.name(), controllerId);
    LOGGER.info("Delete l2vpn end.");
    return Response.status(Response.Status.OK)
        .entity(l2vpn).build();
  }


  private String getSouthElineId(String uuid, String controllerId)
      throws ResourceNotFoundException {
    UuidMap uuidMap = uuidMapDao.get(uuid, UuidMap.UuidTypeEnum.ELINE.name(),
        controllerId);
    if (uuidMap == null) {
      throw new ResourceNotFoundException("L3vpn " + uuid);
    }
    return uuidMap.getExternalId();
  }

}
