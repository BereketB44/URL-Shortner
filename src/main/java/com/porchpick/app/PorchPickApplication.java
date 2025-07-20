package com.porchpick.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PorchPickApplication {

    @Value("${spring.application.name}")
	private String appName;
    
    public static void main(String[] args) {
        SpringApplication.run(PorchPickApplication.class, args);
    }

    @Bean
    public String showAppInfo() {
        System.out.println(">>>>>>>>>> Application '" + appName + "' has started. <<<<<<<<<<");
        return "";
    }
}

