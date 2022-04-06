package com.supremepole.activitimodeler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class ActivitiModelerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiModelerApplication.class, args);
    }

}
