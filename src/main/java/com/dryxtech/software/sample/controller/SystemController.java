package com.dryxtech.software.sample.controller;

import com.dryxtech.software.sample.service.SystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/system")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping(value = "/name")
    public ResponseEntity<String> getName() {
        return ResponseEntity.ok(systemService.getApplicationName());
    }

    @GetMapping(value = "/version")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(systemService.getApplicationVersion());
    }

    @GetMapping(value = "/description")
    public ResponseEntity<String> getDescription() {
        return ResponseEntity.ok(systemService.getApplicationDescription());
    }
}
