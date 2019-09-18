package com.meituan.lyrebird.client.listeners;

import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class Junit4Listener extends RunListener {
    
    @Override
    public void testStarted(Description description)
            throws NoSuchMethodException, SecurityException, LyrebirdClientException {
        Lyrebird lyrebird = new Lyrebird(Lyrebird.getRemoteAddress());
        lyrebird.activate(description.getTestClass().getMethod(description.getMethodName()));
    }
}
