package com.welb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 程序入口  项目启动类
 */
@SpringBootApplication
@EnableScheduling
public class KaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(KaoApplication.class, args);
    }
}
