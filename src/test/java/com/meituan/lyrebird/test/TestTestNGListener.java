package com.meituan.lyrebird.test;

import com.google.gson.Gson;
import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.MockData;
import com.meituan.lyrebird.client.api.*;
import com.meituan.lyrebird.client.events.TestNGListener;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import okhttp3.mockwebserver.*;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

@MockData(groupID = "89e0426c-9cf9-454a-bbe0-94246fc23b04", groupName = "首页")
@Listeners(TestNGListener.class)
public class TestTestNGListener {
    private MockWebServer mockServer;
    private Gson gson;
    private Lyrebird lyrebird;

    @BeforeClass
    public void setup() throws IOException {
        this.gson = new Gson();
        this.mockServer = new MockWebServer();
        this.mockServer.start(9090);
        this.lyrebird = new Lyrebird();
        makeSuccessResponse();
    }

    @AfterClass
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
        RecordedRequest req = mockServer.takeRequest();
        Assert.assertEquals(req.getPath(), "/api/mock/89e0426c-9cf9-454a-bbe0-94246fc23b04/activate", "request path not match");
    }
}