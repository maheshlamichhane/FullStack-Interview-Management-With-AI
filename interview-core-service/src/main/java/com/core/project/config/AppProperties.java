package com.core.project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "build")
@RefreshScope
@Getter
@Setter
@Configuration
public class AppProperties {
    private String version;
}
