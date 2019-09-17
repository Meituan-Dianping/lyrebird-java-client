package com.meituan.lyrebird.client.events;

import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Field;

public class TestNGListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        try {
            Lyrebird lyrebird = null;
            for (Field field : result.getTestClass().getRealClass().getDeclaredFields()) {
                if (field.getType() == Lyrebird.class) {
                    field.setAccessible(true);
                    lyrebird = (Lyrebird) field.get(result.getInstance());
                }
            }

            if (lyrebird == null) {
                return;
            }

            lyrebird.activate(result.getMethod().getConstructorOrMethod().getMethod());
        } catch (LyrebirdClientException | IllegalAccessException e) {
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
