package com.meituan.lyrebird.test;

import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import okhttp3.mockwebserver.*;
import org.junit.*;

import java.io.IOException;

public class TestLyrebirdClient {
    private MockWebServer mockServer;
    private Lyrebird lyrebird;

    @Before
    public void setup() {
        this.mockServer = new MockWebServer();
    }

    @After
    public void teardown() throws IOException {
        if (this.mockServer != null) {
            this.mockServer.close();
        }
    }

    @Test
    public void testLyrebirdClientWithDefaultPort() throws IOException, LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
                .setBody(
                        "{\"code\": 1000,\"ip\": \"localhost\",\"message\": \"success\",\"mock.port\": 9090,\"proxy.port\": 4272}"
                ));

        this.mockServer.start(9090);
        this.lyrebird = new Lyrebird();

        Assert.assertEquals("success", lyrebird.getLyrebirdStatus().getMessage());
    }

    @Test
    public void testLyrebirdClientWithManualPort() throws IOException, LyrebirdClientException {
        this.mockServer.enqueue(new MockResponse()
                .setBody(
                        "{\"code\": 1000,\"ip\": \"localhost\",\"message\": \"success\",\"mock.port\": 8080,\"proxy.port\": 4272}"
                ));

        this.mockServer.start();
        this.lyrebird = new Lyrebird("http://localhost:" + this.mockServer.getPort());

        Assert.assertEquals("success", lyrebird.getLyrebirdStatus().getMessage());
    }
}
