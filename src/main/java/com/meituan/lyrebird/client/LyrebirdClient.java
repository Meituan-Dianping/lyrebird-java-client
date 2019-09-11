package com.meituan.lyrebird.client;

import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import com.meituan.lyrebird.client.api.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class LyrebirdClient {
    private LyrebirdService lyrebirdService;

    public LyrebirdClient(String lyrebirdRemoteAddress) {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(lyrebirdRemoteAddress)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        lyrebirdService = retrofit.create(LyrebirdService.class);
    }

    /**
     * Get lyrebird services status
     *
     * @return Status
     * @throws LyrebirdClientException
     */
    public Status status() throws LyrebirdClientException {
        Status resp;
        try {
            resp = lyrebirdService.status().execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while get status", e);
        }
        if (resp == null) {
            throw new LyrebirdClientException("Got none response from status request");
        }
        if (resp.getCode() != 1000) {
            throw new LyrebirdClientException(resp.getMessage());
        }
        return resp;
    }

    /**
     * Activate lyrebird mock data group by group ID
     *
     * @param groupID
     * @throws LyrebirdClientException
     */
    public void activate(String groupID) throws LyrebirdClientException {
        BaseResponse resp;
        try {
            resp = lyrebirdService.activate(groupID).execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while activate data", e);
        }
        if (resp == null) {
            throw new LyrebirdClientException("Got none response from activate request");
        }
        if (resp.getCode() != 1000) {
            throw new LyrebirdClientException(resp.getMessage());
        }
    }

    /**
     * Deactivate lyrebird mock data group
     * @throws LyrebirdClientException
     */
    public void deactivate() throws LyrebirdClientException {
        BaseResponse resp;
        try {
            resp = lyrebirdService.deactivate().execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while deactivate data", e);
        }

        if (resp == null) {
            throw new LyrebirdClientException("Got none response from deactivate request");
        }
        if (resp.getCode() != 1000) {
            throw new LyrebirdClientException(resp.getMessage());
        }
    }

    /**
     * Get a set of flow data from lyrebird API
     *
     * @return An array list contains Flows data
     * @throws LyrebirdClientException
     */
    public Flow[] getFlowList() throws LyrebirdClientException {
        try {
            return lyrebirdService.getFlowList().execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while find flows", e);
        }
    }

    /**
     * Get Lyrebird flow data detail
     *
     * @param flowId 每条flow data都对应一个ID标识，通过ID可以查询该条数据的详细信息
     * @return A flow which found by ID
     * @throws LyrebirdClientException
     */
    public FlowDetail getFlowDetail(String flowId) throws LyrebirdClientException {
        try {
            return lyrebirdService.getFlowDetail(flowId).execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while find flow by ID", e);
        }
    }

    /**
     * Clear all flow data
     *
     * @throws LyrebirdClientException
     */
    public void clearFlowList() throws LyrebirdClientException {
        BaseResponse resp;
        try{
            resp = lyrebirdService.clearFlowList(new Flow()).execute().body();
        }catch (IOException e){
            throw new LyrebirdClientException("Catch exception while clear flow data",e);
        }
        if(resp==null){
            throw new LyrebirdClientException("Got none response from delete /api/flow");
        }
        if(resp.getCode()!=1000){
            throw new LyrebirdClientException(resp.getMessage());
        }
    }
}
