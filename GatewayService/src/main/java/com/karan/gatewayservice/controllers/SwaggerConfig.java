package com.karan.gatewayservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class SwaggerConfig {

    private final GatewayProperties properties;

    public SwaggerConfig(GatewayProperties properties) {
        this.properties = properties;
    }

    @Bean
    public JsonSerializer jsonSerializer(List<JacksonModuleRegistrar> moduleRegistrars) {
        return new JsonSerializer(moduleRegistrars);
    }

    @Primary
    @Bean
    @Lazy
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> properties.getRoutes().stream()
                .map(route -> createResource(route.getId(), getRouteLocation(route), "2.0"))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private String getRouteLocation(RouteDefinition route) {
        String s1 = Optional.ofNullable(route.getPredicates().get(0).getArgs().values().toArray()[0])
                .map(String::valueOf)
                .map(s -> s.replace("*", ""))
                .orElse(null);

        return s1;
    }

    private SwaggerResource createResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location + "/v2/api-docs");
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}

