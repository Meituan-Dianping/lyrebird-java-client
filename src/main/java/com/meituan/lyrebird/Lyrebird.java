package com.meituan.lyrebird;

import com.meituan.lyrebird.client.api.bandwidth.Bandwidth;
import com.meituan.lyrebird.client.api.bandwidth.SpeedLimit;
import io.socket.client.Socket;
import java.lang.reflect.Method;

import com.meituan.lyrebird.client.LyrebirdClient;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;

import com.meituan.lyrebird.client.api.*;
import java.net.URISyntaxException;

public class Lyrebird {
    private LyrebirdClient client;
    private static ThreadLocal<String> remoteAddress = ThreadLocal.withInitial(() -> "http://localhost:9090/");

    public Lyrebird() {
        this(remoteAddress.get());
    }

    public Lyrebird(String lyrebirdRemoteAddress) {
        client = new LyrebirdClient(lyrebirdRemoteAddress);
    }

    /**
     * set remote lyrebird server address
     * 
     * @param url e.g http://localhost:9090
     */
    public static void setRemoteAddress(String url) {
        remoteAddress.set(url);
    }

    /**
     * get remote lyrebird server address
     * 
     * @return remoteAddress Lyrebird server address
     */
    public static String getRemoteAddress() {
        return remoteAddress.get();
    }

    /**
     * Activate lyrebird mock data group by group ID
     *
     * @throws LyrebirdClientException
     */
    public void activate(String groupID) throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        client.activate(groupID);
    }

    /**
     * Activate lyrebird mock data group by @MockData annotation
     * 
     * @param method test method
     * @throws LyrebirdClientException
     */
    public void activate(Method method) throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        client.activate(method);
    }

    /**
     * Deactivate lyrebird mock data group
     *
     * @throws LyrebirdClientException
     */
    public void deactivate() throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        client.deactivate();
    }

    /**
     * Get lyrebird services status
     *
     * @return Status
     * @throws LyrebirdClientException
     */
    public Status getLyrebirdStatus() throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        return client.status();
    }

    /**
     * Get a set of flow data from lyrebird API
     *
     * @return An array list contains Flows data
     * @throws LyrebirdClientException
     */
    public Flow[] getFlowList() throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        return client.getFlowList();
    }

    /**
     * Get Lyrebird flow data detail
     *
     * @param flowID The unique indication of the flow data
     * @return A flow detail which found by ID
     * @throws LyrebirdClientException
     */
    public FlowDetail getFlowDetail(String flowID) throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        return client.getFlowDetail(flowID);
    }

    /**
     * Clear all flow data
     *
     * @throws LyrebirdClientException
     */
    public void clearFlowList() throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        client.clearFlowList();
    }

    /**
     * Get event list by channel
     * 
     * @param channel channel name
     * @return Events a list of event
     * @throws LyrebirdClientException
     */
    public EventDetail[] getEventList(String channel) throws LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        if (client.getEventList(channel) == null){
            return null;
        }
        return client.getEventList(channel).getEvents();
    }

    /**
     * Get mock data by data id
     *
     * @param dataId mock data id
     * @return
     * @throws LyrebirdClientException
     */
    public LBMockData getMockData(String dataId) throws LyrebirdClientException {
        return client.getMockData(dataId);
    }

    /**
     * set the speed limit
     *
     * @param bandwidth BANDWIDTH_2G对应2G；BANDWIDTH_2_5G对应2.5G；BANDWIDTH_3G对应3G；UNLIMITED对应无限制
     * @throws LyrebirdClientException
     */
    public void setSpeedLimit(Bandwidth bandwidth) throws LyrebirdClientException {
        client.setSpeedLimit(bandwidth);
    }

    /**
     * get the speed limit bandwidth
     *
     * @return
     * @throws LyrebirdClientException
     */
    public SpeedLimit getSpeedLimit() throws LyrebirdClientException {
        return client.getSpeedLimit();
    }

    /**
     * Get an object of socket io
     *
     * @return
     * @throws URISyntaxException
     */
    public Socket getSocketInstance() throws URISyntaxException, LyrebirdClientException {
        if (client == null) {
            throw new LyrebirdClientException("Please start lyrebird server before call this function");
        }
        return client.getSocketInstance(remoteAddress.get());
    }
}
