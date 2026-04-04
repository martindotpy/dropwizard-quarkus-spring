package dev.martindotpy.dropwizardquarkusspring.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "dev.martindotpy.dropwizardquarkusspring" })
public class SpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }
}
