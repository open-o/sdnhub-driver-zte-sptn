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

package org.openo.sdno.sptndriver.services;

import com.google.gson.Gson;

import org.openo.sdno.sptndriver.converter.L2Converter;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.SCmdResultAndNcdResRelationsOutput;
import org.openo.sdno.sptndriver.models.south.SCommandResultOutput;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SDeleteElineInput;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;



/**
 * E-Line service CRUD.
 */
public class ElineService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ElineService.class);
  private String baseUrl;

  /**
   * The constructor
   *
   * @param baseUrl URL of SPTN controller.
   */
  public ElineService(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Send REST to controller to create E-Line and tunnels.
   *
   * @param createElineAndTunnels E-Line and tunnels information
   */
  public String createElineAndTunnels(SCreateElineAndTunnelsInput createElineAndTunnels)
      throws HttpErrorException, IOException, CommandErrorException {
    String printText = "Create eline and tunnels "
        + createElineAndTunnels.getInput().getSncEline().getId();
    LOGGER.debug(printText + " begin. ");
    Gson gson = new Gson();
    LOGGER.debug("Send to Controller: " + gson.toJson(createElineAndTunnels));
    Retrofit retrofit = ServiceUtil.initRetrofit(baseUrl);
    ElineServiceInterface service = retrofit.create(ElineServiceInterface.class);
    Call<SCmdResultAndNcdResRelationsOutput> repos
        = service.createElineAndTunnels(createElineAndTunnels);
    Response<SCmdResultAndNcdResRelationsOutput> response = repos.execute();
    ServiceUtil.parseCmdResultAndNcdResRelOutput(response, LOGGER, printText);
    LOGGER.debug(printText + " end. ");
    return L2Converter.getReturnId(response.body());
  }

  /**
   * Send REST to controller to delete E-Line.
   *
   * @param elineId E-Line UUID
   */
  public void deleteEline(SDeleteElineInput elineId)
      throws IOException, HttpErrorException, CommandErrorException {
    String printText = "Delete Eline " + elineId.getInput().getElineId();
    LOGGER.debug(printText + " begin. ");
    Retrofit retrofit = ServiceUtil.initRetrofit(baseUrl);
    ElineServiceInterface service = retrofit.create(ElineServiceInterface.class);
    Call<SCommandResultOutput> repos = service.deleteEline(elineId);
    Response<SCommandResultOutput> response = repos.execute();
    ServiceUtil.parseRpcResult(response, LOGGER, printText);
    LOGGER.debug(printText + " end. ");
  }

}
