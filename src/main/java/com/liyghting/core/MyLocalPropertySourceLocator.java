package com.liyghting.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * local config file locator
 */
@Order(0)
public class MyLocalPropertySourceLocator implements PropertySourceLocator {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MyLocalPropertySourceLocator.class);
    private static final String MYLOCAL_PROPERTY_SOURCE_NAME = "MYLOCAL";

    @Override
    public PropertySource<?> locate(Environment environment) {

        CompositePropertySource composite = new CompositePropertySource(
                MYLOCAL_PROPERTY_SOURCE_NAME);
        String dataIdPrefix = environment.getProperty("spring.application.name");
        String fileExtension = ".yaml";
        PropertySourceLoader propertySourceLoader = new YamlPropertySourceLoader();

        URL baseClassesUrl = this.getClass().getClassLoader().getResource("");
        File baseClassesFile = new File(baseClassesUrl.getFile());
        String basePath = baseClassesFile.getParentFile().getParentFile().getAbsolutePath();

        Resource resource = new FileSystemResource(basePath + "/config/" + dataIdPrefix + fileExtension);
        try {
            composite.addFirstPropertySource(propertySourceLoader.load(dataIdPrefix + fileExtension, resource).get(0));
        } catch (IOException e) {
            LOGGER.warn("can not load property source {}, exception: {}", dataIdPrefix + fileExtension, e);
        }
        for (String activeProfile : environment.getActiveProfiles()) {
            try {
                resource = resource.createRelative(dataIdPrefix + "-" + activeProfile + fileExtension);
                composite.addFirstPropertySource(propertySourceLoader.load(dataIdPrefix + "-" + activeProfile + fileExtension, resource).get(0));
            } catch (IOException e) {
                LOGGER.warn("can not load property source {}, exception: {}", dataIdPrefix + "-" + activeProfile + fileExtension, e);
            }
        }

        return composite;
    }
}
