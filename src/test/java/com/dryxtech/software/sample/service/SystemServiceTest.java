package com.dryxtech.software.sample.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemServiceTest {

    private SystemService systemService;

    @BeforeEach
    void setUp() {
        this.systemService = new SystemService("test-name", "test-description", "test-version");
    }

    @Test
    void getApplicationName() {
        assertEquals("test-name", systemService.getApplicationName());
    }

    @Test
    void getApplicationDescription() {
        assertEquals("test-description", systemService.getApplicationDescription());
    }

    @Test
    void getApplicationVersion() {
        assertEquals("test-version", systemService.getApplicationVersion());
    }
}
