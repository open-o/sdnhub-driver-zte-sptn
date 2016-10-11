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
import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.converter.L3Converter;
import org.openo.sdno.sptndriver.db.dao.UuidMapDao;
import org.openo.sdno.sptndriver.db.model.UuidMap;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NL3Vpn;
import org.openo.sdno.sptndriver.models.south.SL3vpn;
import org.openo.sdno.sptndriver.services.L3Service;
import org.openo.sdno.sptndriver.utils.EsrUtil;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.validation.Validator;
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

@Path("/openoapi/sbi-l3vpn/v1")
@Api(tags = {"L3vpn API"})
@Produces(MediaType.APPLICATION_JSON)
/**
 *  The class of L3vpn resource.
 */
public class L3Resource {

  private final Validator validator;
  private final UuidMapDao uuidMapDao;
  private Config config;

  public L3Resource(Validator validator, Config config, DBI jdbi) {
    this.validator = validator;
    this.config = config;
    this.uuidMapDao = jdbi.onDemand(UuidMapDao.class);
  }

  /**
   * Create L3vpn.
   *
   * @param l3vpn L3vpn parameters.
   * @return 201 if success.
   */
  @POST
  @Path("/l3vpns")
  @ApiOperation(value = "Create a L3vpn connection",
      code = HttpStatus.CREATED_201,
      response = NL3Vpn.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.BAD_REQUEST_400,
          message = "Create a L3Vpn connection failure as parameters invalid.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNAUTHORIZED_401,
          message = "Unauthorized",
          response = String.class),
      @ApiResponse(code = HttpStatus.NOT_FOUND_404,
          message = "Create a L3Vpn connection failure as can't reach server.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
          message = "Unprocessable L3vpn Entity.",
          response = String.class),
      @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
          message = "Create a L3Vpn connection failure as inner error.",
          response = String.class)
      })
  @Produces(MediaType.APPLICATION_JSON)
  @Timed
  public Response createL3vpn(@ApiParam(value = "L2vpn information", required = true)
                                                     NL3Vpn l3vpn,
                                               @ApiParam(value = "Controller uuid, "
                                                   + "the format is X-Driver-Parameter:extSysID={ctrlUuid}",
                                                   required = true)
                                               @HeaderParam("X-Driver-Parameter") String controllerIdPara)
      throws URISyntaxException {
    String controllerId = ServiceUtil.getControllerId(controllerIdPara);
    SL3vpn southL3vpn = L3Converter.convertNbiToSbi(l3vpn);
    if (southL3vpn == null || southL3vpn.getAcList() == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .type(MediaType.TEXT_PLAIN_TYPE)
          .entity("Input L3vpn can not be converted to south L3vpn.")
          .build();
    }

    try {
      L3Service l3Service = new L3Service(
          EsrUtil.getSdnoControllerUrl(controllerId, config));
      l3Service.createL3vpn(southL3vpn);
    } catch (HttpErrorException ex) {
      return ex.getResponse();
    } catch (IOException ex) {
      return Response
          .status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(ExceptionUtils.getStackTrace(ex))
          .type(MediaType.TEXT_PLAIN_TYPE)
          .build();
    } catch (CommandErrorException ex) {
      return ex.getResponse();
    }
    String externalId = l3vpn.getId();
    uuidMapDao.insert(l3vpn.getId(), externalId, UuidMap.UuidTypeEnum.L3VPN.name(), controllerId);
    return Response.status(Response.Status.CREATED)
        .entity(l3vpn).build();

  }

  /**
   * Delete L3vpn.
   *
   * @param vpnid Global UUID of L3vpn(UUID of L3vpn in SDN-O).
   * @return 200 if success
   */
  @DELETE
  @Path("/l3vpns/{vpnid}")
  @ApiOperation(value = "Delete a L3vpn connection",
      code = HttpStatus.OK_200)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatus.BAD_REQUEST_400,
          message = "Delete a L3Vpn connection failure as parameters invalid.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNAUTHORIZED_401,
          message = "Unauthorized",
          response = String.class),
      @ApiResponse(code = HttpStatus.NOT_FOUND_404,
          message = "Delete a L3Vpn connection failure as can't reach server.",
          response = String.class),
      @ApiResponse(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE_415,
          message = "Unprocessable L3vpn Entity.",
          response = String.class),
      @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500,
          message = "Delete a L3Vpn connection failure as inner error.",
          response = String.class)
      })
  @Produces(MediaType.APPLICATION_JSON)
  @Timed
  public Response deleteL3vpn(@ApiParam(value = "L2vpn uuid", required = true)
                                @PathParam("vpnid") String vpnid,
                              @ApiParam(value = "Controller uuid, "
                                  + "the format is X-Driver-Parameter:extSysID={ctrlUuid}",
                                  required = true)
                              @HeaderParam("X-Driver-Parameter") String controllerIdPara)
      throws URISyntaxException {
    String controllerId = ServiceUtil.getControllerId(controllerIdPara);
    String southL3vpnId = getSouthL3vpnId(vpnid, controllerId);
    if (southL3vpnId == null || vpnid == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Can not find L3vpn.")
          .type(MediaType.TEXT_PLAIN_TYPE)
          .build();
    }

    try {
      L3Service l3Service = new L3Service(
          EsrUtil.getSdnoControllerUrl(controllerId, config));
      l3Service.deleteL3vpn(southL3vpnId);
    } catch (HttpErrorException ex) {
      return ex.getResponse();
    } catch (CommandErrorException ex) {
      return ex.getResponse();
    } catch (IOException ex) {
      return Response
          .status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(ExceptionUtils.getStackTrace(ex))
          .type(MediaType.TEXT_PLAIN_TYPE)
          .build();
    }
    uuidMapDao.delete(vpnid, UuidMap.UuidTypeEnum.L3VPN.name(), controllerId);
    // TODO: 2016/9/13 return value should be L3VpnResponse.
    return Response.ok().build();
  }

  private String getSouthL3vpnId(String uuid, String controllerId) {
    return uuidMapDao.get(uuid, UuidMap.UuidTypeEnum.L3VPN.name(),
        controllerId).getExternalId();
  }
}
