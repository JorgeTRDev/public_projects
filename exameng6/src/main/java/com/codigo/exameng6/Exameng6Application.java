package com.codigo.exameng6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Exameng6Application {

	public static void main(String[] args) {
		SpringApplication.run(Exameng6Application.class, args);
	}

}
