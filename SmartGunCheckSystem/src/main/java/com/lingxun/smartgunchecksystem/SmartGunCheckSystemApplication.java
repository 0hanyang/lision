package com.lingxun.smartgunchecksystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Required for @Scheduled tasks later
public class SmartGunCheckSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartGunCheckSystemApplication.class, args);
    }
}
