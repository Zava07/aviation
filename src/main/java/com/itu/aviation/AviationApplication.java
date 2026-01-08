package com.itu.aviation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AviationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AviationApplication.class, args);
		System.out.println("Aviation Application Started Successfully!");
	}

}
