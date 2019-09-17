package com.meituan.lyrebird.client.events;

import com.meituan.lyrebird.Lyrebird;
import com.meituan.lyrebird.client.exceptions.LyrebirdClientException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import java.lang.reflect.Field;

public class Junit4Listener extends RunListener {
    
    @Override
    public void testStarted(Description description) throws SecurityException, IllegalAccessException, LyrebirdClientException, NoSuchMethodException {
        Lyrebird lyrebird = null;
        for (Field field: description.getTestClass().getDeclaredFields()) {
            if (field.getType() == Lyrebird.class) {
                field.setAccessible(true);
                lyrebird = (Lyrebird) field.get(description);
            }
        }

        if (lyrebird == null) {
            return;
        }

        lyrebird.activate(description.getTestClass().getMethod(description.getMethodName()));
    }
}
