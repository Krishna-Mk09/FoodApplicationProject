package com.userauthentication.jdp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.userauthentication.jdp", "com.foodapplication.jdp.Common_Service"})
@EntityScan(basePackages = {"com.userauthentication.jdp.entity", "com.foodapplication.jdp.Common_Service.Entity"})
@EnableJpaRepositories(basePackages = {"com.userauthentication.jdp.repository", "com.foodapplication.jdp.Common_Service.repository"})
@EnableFeignClients
@EnableDiscoveryClient
public class UserAuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAuthenticationServiceApplication.class, args);
    }
}
