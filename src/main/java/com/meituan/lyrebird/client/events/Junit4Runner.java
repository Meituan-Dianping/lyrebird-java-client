package com.meituan.lyrebird.client.events;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class Junit4Runner extends BlockJUnit4ClassRunner {

    public Junit4Runner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
    
    @Override public void run(RunNotifier notifier){
        notifier.addListener(new Junit4Listener());
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }
}