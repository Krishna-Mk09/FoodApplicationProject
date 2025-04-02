package com.order.jdp.orderservice;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.foodapplication.jdp.Common_Service","com.userauthentication.jdp","com.order.jdp.orderservice" })
@EntityScan(basePackages = { "com.foodapplication.jdp.Common_Service.Entity" ,"com.userauthentication.jdp.entity","com.order.jdp.orderservice.entity"})
@EnableJpaRepositories(basePackages = {"com.foodapplication.jdp.Common_Service.repository" ,"com.userauthentication.jdp.repository", "com.order.jdp.orderservice.repository"})
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
