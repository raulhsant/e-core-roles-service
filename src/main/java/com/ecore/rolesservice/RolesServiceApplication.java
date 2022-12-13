package com.ecore.rolesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class RolesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RolesServiceApplication.class, args);
    }

}
