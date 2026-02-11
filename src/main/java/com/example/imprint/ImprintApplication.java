package com.example.imprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.imprint")
public class ImprintApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImprintApplication.class, args);
    }
}