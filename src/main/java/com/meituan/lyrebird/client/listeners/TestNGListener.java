package com.meituan.lyrebird.client.listeners;

import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        Lyrebird lyrebird = new Lyrebird(Lyrebird.getRemoteAddress());
        try {
            lyrebird.activate(result.getMethod().getConstructorOrMethod().getMethod());
        } catch (LyrebirdClientException e) {
            System.out.println(e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestFailure(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        
    }

    @Override
    public void onFinish(ITestContext context) {
        
    }
}
