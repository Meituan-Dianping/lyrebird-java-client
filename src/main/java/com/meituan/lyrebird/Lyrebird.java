package com.meituan.lyrebird;

import java.lang.reflect.Method;

import com.meituan.lyrebird.client.LyrebirdClient;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import com.meituan.lyrebird.client.api.*;


public class Lyrebird {
    private LyrebirdClient client;

    public Lyrebird() {
        this("http://localhost:9090");
    }

    public Lyrebird(String lyrebirdRemoteAddress) {
        client = new LyrebirdClient(lyrebirdRemoteAddress);
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
     * Activate lyrebird mock data group by group ID
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
     * @param flowID 每条flow data都对应一个ID标识，通过ID可以查询该条数据的详细信息
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
}
