package com.kalyansarwa.stockportfolio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

    @Autowired
    HypermediaRestTemplateConfigurer configurer;

    @Bean
    RestTemplate restTemplate() {
        return configurer.registerHypermediaTypes(new RestTemplate());
    }
}