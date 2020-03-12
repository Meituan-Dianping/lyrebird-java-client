package com.meituan.lyrebird.test;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.api.*;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import java.util.List;
import okhttp3.mockwebserver.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class TestFunctional {
    private MockWebServer mockServer;
    private Gson gson;
    private Lyrebird lyrebird;

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
        assertEquals(null, flow.getRequest().getData());
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
        assertEquals(Integer.valueOf(18), flow.getResponse().getData("$.age"));
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
    public void testEventList() throws LyrebirdClientException, InterruptedException {
        this.mockServer.enqueue(new MockResponse()
            .setBody(
                "{\"code\": 1000,\"events\": [{\"channel\": \"page\",\"content\":\"{\\\"page\\\": \\\"com.lyrebird.java.client\\\", \\\"url\\\": null}\",\"event_id\": \"81825f43-5fd6-45f7-b22b-83493ca99e46\",\"id\": 44463,\"timestamp\": 1568277444.738399},{\"channel\": \"page\",\"content\":\"{\\\"page\\\": \\\"com.lyrebird.java.client\\\", \\\"url\\\": \\\"www.lyrebird.com\\\"}\",\"event_id\": \"1b399d1b-b53f-4da9-b2fc-d5576d3d9e58\",\"id\": 44410,\"timestamp\": 1568277440.43741}],\"message\": \"success\",\"page\": 0,\"page_count\": 82,\"page_size\": 20}"
            ));
        
        EventDetail[] eventList = this.lyrebird.getEventList("page");
        RecordedRequest req = this.mockServer.takeRequest();

        Assert.assertEquals("request path not match", "/api/event/page", req.getPath());
        Assert.assertTrue(eventList.length > 0);
        Assert.assertEquals("page", eventList[0].getChannel());
        Assert.assertEquals("com.lyrebird.java.client", JsonPath.parse(eventList[0].getContent()).read("$.page"));
        Assert.assertEquals("81825f43-5fd6-45f7-b22b-83493ca99e46", eventList[0].getEventID());
    }

    @Test
    public void testMockDetails() throws LyrebirdClientException {
    this.mockServer.enqueue(
        new MockResponse()
            .setBody(
                "{\"code\": 1000,\"data\": {\"id\": \"cfa0c589-8ef0-4885-b4f4-b9688c5af0d5\", \"name\": \"test-data\", \"response\": {\"data\": \"[{\\\"type\\\": \\\"scheme\\\", \\\"info\\\":{\\\"value\\\": \\\"test://www.lyrebird.java.sdk.com\\\"}, \\\"desc\\\": \\\"The scheme of target page\\\"}]\"}}, \"message\": \"success\"}"));

        LBMockData lbMockData = this.lyrebird.getMockData("cfa0c589-8ef0-4885-b4f4-b9688c5af0d5");
        Assert.assertEquals("cfa0c589-8ef0-4885-b4f4-b9688c5af0d5", lbMockData.getId());
        Assert.assertEquals("test-data", lbMockData.getName());

        List<String> urlScheme = JsonPath.parse(lbMockData.getResponseData()).read("$[?(@.type == 'scheme')].info.value");
        Assert.assertEquals(1, urlScheme.size());
        Assert.assertEquals("test://www.lyrebird.java.sdk.com", urlScheme.get(0));
    }
}
