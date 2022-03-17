package com.supremepole.activitispringbootbasic;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiSpringBootBasicApplication {

    @Bean
    CommandLineRunner basics(final RuntimeService runtimeService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                runtimeService.startProcessInstanceByKey("waiter", Collections.singletonMap("customerId", (Object) 243L));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ActivitiSpringBootBasicApplication.class, args);
    }

}
