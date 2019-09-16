package com.meituan.lyrebird.client.events;

import com.meituan.lyrebird.Lyrebird;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class Junit4Runner extends BlockJUnit4ClassRunner {

    public Junit4Runner(Class<?> klass) throws InitializationError {
        super(klass);
    }
    
    @Override public void run(RunNotifier notifier){
        notifier.addListener(new Lyrebird());
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }
}