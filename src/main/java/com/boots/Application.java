package com.boots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.LogManager;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(
                    SpringApplication.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e);
        }

        SpringApplication.run(Application.class, args);
    }
}
