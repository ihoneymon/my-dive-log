package me.divelog.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WebApplication.class);
        app.run(args);
    }
}
