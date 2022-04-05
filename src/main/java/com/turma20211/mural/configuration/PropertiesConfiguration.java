package com.turma20211.mural.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {

    public static String PROFILE_ACTIVE;
    public static String API_BASE_URL;
    public static String FRONT_BASE_URL;


    @Value("${spring.profiles.active}")
    public void setProfileActive(String profileActive) {
        PropertiesConfiguration.PROFILE_ACTIVE = profileActive;
    }

    @Value("${api.base.url}")
    public void setBaseUrl(String baseUrl) {
        PropertiesConfiguration.API_BASE_URL = baseUrl;
    }

    @Value("${front.base.url}")
    public void setFrontBaseUrl(String frontBaseUrl) {
        PropertiesConfiguration.FRONT_BASE_URL = frontBaseUrl;
    }
}
