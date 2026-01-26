package com.utn.eventmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class EventmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventmanagerApplication.class, args);
    }

}
