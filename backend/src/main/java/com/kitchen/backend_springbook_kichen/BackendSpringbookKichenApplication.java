package com.kitchen.backend_springbook_kichen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kitchen.backend_springbook_kichen.mapper")  // ← 添加这一行
public class BackendSpringbookKichenApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSpringbookKichenApplication.class, args);
        System.out.println("========================================");
        System.out.println("  私房菜学习与分享系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}