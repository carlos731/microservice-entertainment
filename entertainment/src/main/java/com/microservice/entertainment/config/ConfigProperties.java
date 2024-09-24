package com.microservice.entertainment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "entertainment")
@Getter
@Setter
public class ConfigProperties {
    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;
}
