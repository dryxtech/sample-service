package com.dryxtech.software.sample.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:build.properties")
public class SystemService {

    @Value("${application-name}")
    private String applicationName;

    @Value("${application-description}")
    private String applicationDescription;

    @Value("${application-version}")
    private String applicationVersion;

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }
}
