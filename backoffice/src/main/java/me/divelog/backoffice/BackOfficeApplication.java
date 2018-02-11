package me.divelog.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackOfficeApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BackOfficeApplication.class);
        app.run(args);
    }
}
