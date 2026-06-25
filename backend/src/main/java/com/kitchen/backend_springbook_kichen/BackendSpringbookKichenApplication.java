package com.kitchen.backend_springbook_kichen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kitchen.backend_springbook_kichen.mapper")
public class BackendSpringbookKichenApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSpringbookKichenApplication.class, args);
        System.out.println("=================================================================");
        System.out.println("  Private Kitchen Learning & Sharing System started successfully!");
        System.out.println("  Access URL: http://localhost:8080");
        System.out.println("=================================================================");
    }
}