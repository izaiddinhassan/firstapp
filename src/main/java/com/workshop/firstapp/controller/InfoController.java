package com.workshop.firstapp.controller;

import com.workshop.firstapp.config.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/info")
public class InfoController {
    private final AppProperties appProperties;

    @Value("${server.port}")
    private int serverPort;

    public InfoController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping
    public Map<String, Object> getAppInfo() {
        return Map.of(
                "name", appProperties.getName(),
                "description", appProperties.getDescription(),
                "version", appProperties.getVersion(),
                "port", serverPort,
                "features", Map.of(
                        "validation", appProperties.getFeatures().isValidation(),
                        "errorHandling", appProperties.getFeatures().isErrorHandling()
                )
        );
    }
}