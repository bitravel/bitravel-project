package com.bitravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages= {"com.bitravel"})
public class BitravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitravelApplication.class, args);
	}

}
