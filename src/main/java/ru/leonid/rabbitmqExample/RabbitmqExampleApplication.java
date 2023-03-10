package ru.leonid.rabbitmqExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class RabbitmqExampleApplication {

	public static void main(String[] args) {

		SpringApplication.run(RabbitmqExampleApplication.class, args);
	}

}