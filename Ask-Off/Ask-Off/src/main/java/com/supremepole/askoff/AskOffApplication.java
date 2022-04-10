package com.supremepole.askoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class AskOffApplication {

    public static void main(String[] args) {
        SpringApplication.run(AskOffApplication.class, args);
    }

}
