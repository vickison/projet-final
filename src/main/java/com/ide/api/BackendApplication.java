package com.ide.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		System.out.println("Spring Version: "+ SpringVersion.getVersion());
		System.out.println("Spring Boot Version: "+ SpringBootVersion.getVersion());
	}

}
