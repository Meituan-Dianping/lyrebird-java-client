package com.meituan.lyrebird.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meituan.lyrebird.client.api.bandwidth.Bandwidth;
import com.meituan.lyrebird.client.api.bandwidth.BandwidthTemplate;
import com.meituan.lyrebird.client.api.bandwidth.SpeedLimit;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import com.meituan.lyrebird.client.api.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class LyrebirdClient {
    private LyrebirdService lyrebirdService;
    private Socket socket;

    public LyrebirdClient(
            String lyrebirdRemoteAddress,
            TimeUnit timeUnit,
            long callTimeout,
            long connectTimeout,
            long readTimeout,
            long writeTimeout
    ) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(callTimeout, timeUnit)
                .connectTimeout(connectTimeout,timeUnit)
                .readTimeout(readTimeout,timeUnit)
                .writeTimeout(writeTimeout,timeUnit);

        Retrofit.Builder builder = new Retrofit
                .Builder()
                .baseUrl(lyrebirdRemoteAddress)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(httpClient.build());

        Retrofit retrofit = builder.build();

        // create service
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
     * Activate lyrebird mock data group by @MockData annotation
     *
     * @param method test method
     * @throws LyrebirdClientException
     */
    public void activate(Method method) throws LyrebirdClientException {
        BaseResponse resp;
        MockData mockDataDeclareOnMethod = method.getDeclaredAnnotation(MockData.class);
        MockData mockDataDeclaredOnClass = method.getDeclaringClass().getAnnotation(MockData.class);

        if (mockDataDeclareOnMethod == null && mockDataDeclaredOnClass == null) {
            throw new LyrebirdClientException("Catch exception while activate data, can not found any @MockData annotation declared");
        }
        try {
            if (mockDataDeclareOnMethod != null) {
                resp = lyrebirdService.activate(mockDataDeclareOnMethod.groupID()).execute().body();
            } else {
                resp = lyrebirdService.activate(mockDataDeclaredOnClass.groupID()).execute().body();
            }
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

    /**
     * Get event list by channel
     *
     * @param channel channel name
     * @return Events a list of event
     * @throws LyrebirdClientException
     */
    public Events getEventList(String channel) throws LyrebirdClientException {
        try{
            return lyrebirdService.getEventList(channel).execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while find events by channel ", e);
        }
    }

    /**
     * get mock data by data id
     *
     * @param dataId mock data id
     * @return
     * @throws LyrebirdClientException
     */
    public LBMockData getMockData(String dataId) throws LyrebirdClientException {
        try{
            return lyrebirdService.getMockData(dataId).execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while get mock response data by data id ", e);
        }
    }

    /**
     * set the speed limit
     *
     * @param bandwidth an enum of Bandwidth
     * @throws LyrebirdClientException
     */
    public void setSpeedLimit(Bandwidth bandwidth) throws LyrebirdClientException {
        BandwidthTemplate bandwidthTemplate = new BandwidthTemplate(bandwidth);
        BaseResponse resp;
        try{
            resp =  lyrebirdService.setSpeedLimit(bandwidthTemplate).execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while set the speed limit", e);
        }
        if (resp == null) {
            throw new LyrebirdClientException("Got none response from the speed limit request");
        }
        if (resp.getCode() != 1000) {
            throw new LyrebirdClientException(resp.getMessage());
        }
    }

    /**
     * get the speed limit bandwidth
     *
     * @return
     * @throws LyrebirdClientException
     */
    public SpeedLimit getSpeedLimit() throws LyrebirdClientException {
        try {
            return lyrebirdService.getSpeedLimit().execute().body();
        } catch (IOException e) {
            throw new LyrebirdClientException("Catch exception while get speed limit bandwidth.", e);
        }
    }

    /**
     * Get an object of socket io
     *
     * @param lyrebirdRemoteAddress
     * @return
     * @throws URISyntaxException
     */
    public Socket getSocketInstance(String lyrebirdRemoteAddress) throws URISyntaxException {
        if (socket == null || !socket.connected()) {
            socket = IO.socket(lyrebirdRemoteAddress);
        }
        socket.on(Socket.EVENT_CONNECT, objects -> System.out.println("[Lyrebird Java client Socket IO]: Socket connection established"));
        socket.on(Socket.EVENT_CONNECTING, objects -> System.out.println("[Lyrebird Java client Socket IO]: Socket connecting ..."));
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> System.out.println("[Lyrebird Java client Socket IO]: Connection timeout"));
        socket.on(Socket.EVENT_CONNECT_ERROR, objects -> System.out.println("[Lyrebird Java client Socket IO]: Failed to connect"));
        return socket;
    }
}
