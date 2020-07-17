package com.xiaxinyu.thread.pool;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThreadPoolApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ThreadPoolApplication.class);
        springApplication.run(args);
    }
}
