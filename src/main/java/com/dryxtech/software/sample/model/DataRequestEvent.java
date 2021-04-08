package com.dryxtech.software.sample.model;

import org.springframework.context.ApplicationEvent;

public class DataRequestEvent extends ApplicationEvent {

    public DataRequestEvent(DataRequest<?> source) {
        super(source);
    }
}
