package com.liyghting.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * local config bootstrap
 */
@Configuration
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", havingValue = "false")
public class MyLocalConfigBootstrapConfiguration {

    @Bean
    public MyLocalPropertySourceLocator fyLocalPropertySourceLocator() {
        return new MyLocalPropertySourceLocator();
    }
}
