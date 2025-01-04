package com.furnistyle.furniturebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FurnitureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FurnitureBackendApplication.class, args);
    }

}
