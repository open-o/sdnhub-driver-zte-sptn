/*
 * Copyright 2016-2017 ZTE Corporation.
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

package org.openo.sdnhub.sptndriver.services;

import com.google.gson.Gson;

import org.openo.sdnhub.sptndriver.models.south.SRouteCalReqsInput;
import org.openo.sdnhub.sptndriver.models.south.SRouteCalResultsOutput;
import org.openo.sdnhub.sptndriver.utils.ServiceUtil;
import org.openo.sdnhub.sptndriver.exception.ServerException;
import org.openo.sdnhub.sptndriver.exception.ServerIoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * SBI Tunnel Services.
 */
public class TunnelService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(TunnelService.class);
    private String baseUrl;

    /**
     * The Constructor.
     *
     * @param baseUrl URL of SPTN controller.
     */
    public TunnelService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Calculate route of tunnels.
     *
     * @param routeCalcReqsInput route request.
     * @return route result.
     */
    public SRouteCalResultsOutput calcRoutes(SRouteCalReqsInput routeCalcReqsInput)
        throws ServerException {
        String printText = "Calculate route of tunnel ";
        LOGGER.debug(printText + " begin. ");
        Retrofit retrofit = ServiceUtil.initRetrofit(baseUrl);
        TunnelServiceInterface service = retrofit.create(TunnelServiceInterface.class);
        Gson gson = new Gson();
        LOGGER.debug("Calculate tunnel send to controller is: " + gson.toJson(routeCalcReqsInput));
        Call<SRouteCalResultsOutput> repos = service.calcRoutes(routeCalcReqsInput);
        Response<SRouteCalResultsOutput> response;
        try {
            response = repos.execute();
        } catch (IOException ex) {
            throw new ServerIoException(ex);
        }
        SRouteCalResultsOutput output = ServiceUtil.parseResponse(response, LOGGER, printText);
        LOGGER.debug(printText + " end. ");
        return output;
    }
}
