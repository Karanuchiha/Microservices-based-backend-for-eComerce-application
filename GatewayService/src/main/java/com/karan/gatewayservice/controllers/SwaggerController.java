package com.karan.gatewayservice.controllers;

import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger.web.*;

import java.util.List;

@RestController
public class SwaggerController {
    private final JsonSerializer jsonSerializer;
    private final SwaggerResourcesProvider swaggerResources;

    @Autowired
    public SwaggerController(JsonSerializer jsonSerializer, SwaggerResourcesProvider swaggerResources) {
        this.jsonSerializer = jsonSerializer;
        this.swaggerResources = swaggerResources;
    }

    @RequestMapping({"/swagger-resources/configuration/security"})
    @ResponseBody
    public ResponseEntity<SecurityConfiguration> securityConfiguration() {
        return ResponseEntity.ok(SecurityConfigurationBuilder.builder().build());
    }

    @RequestMapping({"/swagger-resources/configuration/ui"})
    @ResponseBody
    public ResponseEntity<UiConfiguration> uiConfiguration() {
        return ResponseEntity.ok(UiConfigurationBuilder.builder().build());
    }
    @RequestMapping({"/swagger-resources"})
    @ResponseBody
    public ResponseEntity<List<SwaggerResource>> swaggerResources() {
        return ResponseEntity.ok(this.swaggerResources.get());
    }

    @RequestMapping(
            value = {"/v2/api-docs"},
            method = {RequestMethod.GET},
            produces = {"application/json", "application/hal+json"}
    )
    @ResponseBody
    public ResponseEntity<Json> getDocumentation() {
        Swagger swagger = new Swagger();
        return ResponseEntity.ok(this.jsonSerializer.toJson(swagger));
    }
}
