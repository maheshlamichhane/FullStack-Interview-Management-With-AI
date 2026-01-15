package com.interview.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
@SpringBootApplication
public class InterviewManagingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewManagingSystemApplication.class, args);
    }

}
