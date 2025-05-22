package com.workshop.firstapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app")
@Component
public class AppProperties {
    private String name;
    private String description;
    private String version;
    private Features features = new Features();
    private Admin admin = new Admin();

    @Data
    public static class Features {
        private boolean validation;
        private boolean errorHandling;
    }

    @Data
    public static class Admin {
        private List<String> emails;
    }
}