package com.in28minutes.microservices.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayLocator(RouteLocatorBuilder builder) {
        Function<PredicateSpec, Buildable<Route>> routeFunction =
                p -> p.path("/get").filters(f -> f.addRequestParameter("param", "param1")
                        .addRequestHeader("myHeader", "myURI")).uri("http://httpbin.org:80");

        Function<PredicateSpec, Buildable<Route>> routeFunction1 = p -> p.path("/currency-exchange/**").uri("lb://currency-exchange");
        Function<PredicateSpec, Buildable<Route>> routeFunction2 = p -> p.path("/currency-conversion/**").uri("lb://currency-conversion");
        Function<PredicateSpec, Buildable<Route>> routeFunction3 = p -> p.path("/currency-conversion-feign/**").uri("lb://currency-conversion");
        Function<PredicateSpec, Buildable<Route>> routeFunction4 = p -> p.path("/currency-conversion-new/**").filters(f->f.rewritePath("/currency-conversion-new/(?<segment>.*)","/currency-conversion-feign/${segment}")).uri("lb://currency-conversion");
        return builder.routes().route(routeFunction).route(routeFunction1).route(routeFunction2).route(routeFunction3).route(routeFunction4).build();
    }
}
