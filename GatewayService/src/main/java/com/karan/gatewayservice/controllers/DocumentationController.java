//package com.bearingpoint.gatewayservice.controllers;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@EnableAutoConfiguration
//@Component
//@Primary
/**
 * This class was used to add swagger resources when zuul gateway was used and therefore it has some zuul specific properties that do not exist in this project. 
 */

//public class DocumentationController implements SwaggerResourcesProvider {
//
//    public static final String VERSION = "2.0";
//    private static final String API_DOCS = "/%s/v2/api-docs";
//
//    @Value("${zuul.routes.users.serviceId}")
//    private String userServiceId;
//    @Value("${zuul.routes.reviews.serviceId}")
//    private String reviewServiceId;
//    @Value("${zuul.routes.products.serviceId}")
//    private String productServiceId;
//    @Value("${zuul.routes.sales.serviceId}")
//    private String salesServiceId;
//
//    @Override
//    public List get() {
//        List<Object> resources = new ArrayList<>();
//        resources.add(swaggerResource(userServiceId, String.format(API_DOCS, userServiceId), VERSION));
//        resources.add(swaggerResource(reviewServiceId, String.format(API_DOCS, reviewServiceId),
//                VERSION));
//        resources.add(swaggerResource(productServiceId, String.format(API_DOCS, productServiceId),
//                VERSION));
//        resources
//                .add(swaggerResource(salesServiceId, String.format(API_DOCS, salesServiceId), VERSION));
//        return resources;
//    }
//
//    private SwaggerResource swaggerResource(String name, String location, String version) {
//        SwaggerResource swaggerResource = new SwaggerResource();
//        swaggerResource.setName(name);
//        swaggerResource.setLocation(location);
//        swaggerResource.setSwaggerVersion(version);
//        return swaggerResource;
//    }
//
//}

