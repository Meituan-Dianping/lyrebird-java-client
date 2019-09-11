package com.meituan.lyrebird.test;

import com.google.gson.Gson;
import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.MockData;
import com.meituan.lyrebird.client.api.*;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import okhttp3.mockwebserver.*;
import org.junit.*;
import org.junit.rules.TestName;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

@MockData(groupID = "89e0426c-9cf9-454a-bbe0-94246fc23b04", groupName = "首页")
public class TestFunctional {
    private MockWebServer mockServer;
    private Gson gson;
    private Lyrebird lyrebird;

    @Rule 
    public TestName name = new TestName();

    @Before
    public void setup() throws IOException {
        this.gson = new Gson();
        this.mockServer = new MockWebServer();
        this.mockServer.start(9090);
        this.lyrebird = new Lyrebird();
    }

    @After
    public void teardown() throws IOException {
        if(this.mockServer!=null) {
            this.mockServer.close();
            this.mockServer = null;
        }
    }

    private void makeSuccessResponse(){
        BaseResponse resp = new BaseResponse();
        resp.setCode(1000);
        resp.setMessage("success");
        this.mockServer.enqueue(new MockResponse().setBody(gson.toJson(resp)));
    }

    @Test
    public void testActivate() throws InterruptedException, LyrebirdClientException {
        this.makeSuccessResponse();

        this.lyrebird.activate("TestGroupID");

        RecordedRequest req = this.mockServer.takeRequest();
        Assert.assertEquals("request path not match", "/api/mock/TestGroupID/activate", req.getPath());
    }

    @Test
    public void testDeactivate() throws InterruptedException, LyrebirdClientException {
        this.makeSuccessResponse();

        this.lyrebird.deactivate();

        RecordedRequest req = this.mockServer.takeRequest();
        Assert.assertEquals("request path not match", "/api/mock/groups/deactivate", req.getPath());
    }

    @Test
    public void testStatus() throws InterruptedException, LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "{\"code\": 1000,\"ip\": \"192.168.164.79\",\"message\": \"success\",\"mock.port\": 9090,\"proxy.port\": 4272}"
            ));

        Status res = this.lyrebird.getLyrebirdStatus();

        Assert.assertEquals("192.168.164.79", res.getIp());
        Assert.assertEquals(9090, res.getMockPort());
        Assert.assertEquals(4272, res.getProxyPort());

        RecordedRequest req = this.mockServer.takeRequest();
        Assert.assertEquals("", "/api/status", req.getPath());
    }

    @Test
    public void testGetFlowList() throws LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "[{\"id\": \"67ea0002-9566-41db-8178-ca0c2f82a71a\",\"size\": 9003,\"duration\": \"0.33454108238220215\",\"start_time\": 1560152882.706479,\"request\": {\"url\": \"http://www.lyrebird.java.client.com/api/example\",\"path\": \"/api/example\",\"host\": \"http://www.lyrebird.java.client.com/\",\"method\": \"POST\"},\"response\": {\"code\": 200,\"mock\": \"proxy\"}}]"
            ));

        Flow[] flows = this.lyrebird.getFlowList();
        assertEquals("67ea0002-9566-41db-8178-ca0c2f82a71a", flows[0].getId());
    }

    @Test
    public void testGetFlow() throws LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "{\"code\":1000,\"data\":{\"duration\": \"0.33454108238220215\",\"id\": \"67ea0002-9566-41db-8178-ca0c2f82a71a\",\"start_time\":\"1566805867.517032\",\"request\":{\"url\":\"http://www.lyrebird.java.client.com/api/example\",\"query\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"},\"data\":{\"orderId\":\"0\"}},\"response\":{\"code\":200,\"headers\":{\"name\":\"tester\"},\"data\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}}},\"message\":\"success\"}"
            ));

        FlowDetail flow = this.lyrebird.getFlowDetail("67ea0002-9566-41db-8178-ca0c2f82a71a");

        assertEquals("0", flow.getRequest().getData().get("orderId"));
    }

    @Test
    public void testFlowRequest() throws LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "{\"code\":1000,\"data\":{\"duration\": \"0.33454108238220215\",\"id\": \"67ea0002-9566-41db-8178-ca0c2f82a71a\",\"start_time\":\"1566805867.517032\",\"request\":{\"url\":\"http://www.lyrebird.java.client.com/api/example\",\"query\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}},\"response\":{\"code\":200,\"headers\":{\"name\":\"tester\"},\"data\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}}},\"message\":\"success\"}"
            ));

        FlowDetail flow = this.lyrebird.getFlowDetail("67ea0002-9566-41db-8178-ca0c2f82a71a");

        assertEquals("tester", flow.getRequest().getQuery().get("name"));
        assertEquals(null, flow.getRequest().getData().get("age"));
        assertEquals("http://www.lyrebird.java.client.com/api/example", flow.getRequest().getUrl());
    }

    @Test
    public void testFlowResponse() throws LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "{\"code\":1000,\"data\":{\"duration\": \"0.33454108238220215\",\"id\": \"67ea0002-9566-41db-8178-ca0c2f82a71a\",\"start_time\":\"1566805867.517032\",\"request\":{\"query\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}},\"response\":{\"code\":200,\"headers\":{\"name\":\"tester\"},\"data\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}}},\"message\":\"success\"}"
            ));

        FlowDetail flow = this.lyrebird.getFlowDetail("67ea0002-9566-41db-8178-ca0c2f82a71a");

        assertEquals(200, flow.getResponse().getCode());
        assertEquals("tester", flow.getResponse().getHeaders().get("name"));
        assertEquals(18, flow.getResponse().getData("$.age"));
        assertEquals("15.58", flow.getResponse().getData("$.price", String.class));
    }

    @Test
    public void testFlowDuration() throws LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "{\"code\":1000,\"data\":{\"duration\": \"0.33454108238220215\",\"id\": \"67ea0002-9566-41db-8178-ca0c2f82a71a\",\"start_time\":\"1566805867.517032\",\"request\":{\"query\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}},\"response\":{\"code\":200,\"headers\":{\"name\":\"tester\"},\"data\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}}},\"message\":\"success\"}"
            ));

        FlowDetail flow = this.lyrebird.getFlowDetail("67ea0002-9566-41db-8178-ca0c2f82a71a");
        assertEquals(0.33, flow.getFlow().getDuration(), 2);
    }

    @Test
    public void testFlowStartTime() throws LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "{\"code\":1000,\"data\":{\"duration\": \"0.33454108238220215\",\"id\": \"67ea0002-9566-41db-8178-ca0c2f82a71a\",\"start_time\":\"1566805867.517032\",\"request\":{\"query\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}},\"response\":{\"code\":200,\"headers\":{\"name\":\"tester\"},\"data\":{\"name\":\"tester\",\"age\":18,\"price\":\"15.58\"}}},\"message\":\"success\"}"
            ));

        FlowDetail flow = this.lyrebird.getFlowDetail("67ea0002-9566-41db-8178-ca0c2f82a71a");
        assertEquals(1566805867.51, flow.getFlow().getStartTime(), 2);
    }
    
    @Test
    @MockData(groupID = "89e0426c-9cf9-454a-bbe0-94246fc23b04", groupName = "首页")
    public void testActivateByMethodAnnotation() throws LyrebirdClientException, InterruptedException, NoSuchMethodException, SecurityException {
        this.makeSuccessResponse();

        this.lyrebird.activate(this.getClass().getDeclaredMethod(name.getMethodName()));
        RecordedRequest req = this.mockServer.takeRequest();
        Assert.assertEquals("request path not match", "/api/mock/89e0426c-9cf9-454a-bbe0-94246fc23b04/activate", req.getPath());
    }

    @Test
    public void testActivateByClassAnnotation() throws LyrebirdClientException, InterruptedException, NoSuchMethodException, SecurityException {
        this.makeSuccessResponse();

        this.lyrebird.activate(this.getClass().getDeclaredMethod(name.getMethodName()));
        RecordedRequest req = this.mockServer.takeRequest();
        Assert.assertEquals("request path not match", "/api/mock/89e0426c-9cf9-454a-bbe0-94246fc23b04/activate", req.getPath());
    }
}
