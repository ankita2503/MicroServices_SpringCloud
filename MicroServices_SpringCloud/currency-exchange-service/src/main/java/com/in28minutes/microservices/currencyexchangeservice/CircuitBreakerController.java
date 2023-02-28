package com.in28minutes.microservices.currencyexchangeservice;

import ch.qos.logback.classic.Logger;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private Logger logger = (Logger) LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
    @CircuitBreaker(name = "sample-api", fallbackMethod = "hardCodedResponse")
    public String sampleApi() {
        logger.info("sample API received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost/8090/get-dummy-url",String.class);
        return forEntity.getBody();
    }

    public String hardCodedResponse(Exception ex){
        return "fallback-response";
    }

}
