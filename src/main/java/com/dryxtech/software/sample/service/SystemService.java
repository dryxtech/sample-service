package com.dryxtech.software.sample.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:build.properties")
public class SystemService {

    private final String applicationName;
    private final String applicationDescription;
    private final String applicationVersion;

    public SystemService(@Value("${application-name}") String applicationName,
                         @Value("${application-description}") String applicationDescription,
                         @Value("${application-version}") String applicationVersion) {
        this.applicationName = applicationName;
        this.applicationDescription = applicationDescription;
        this.applicationVersion = applicationVersion;
    }

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
