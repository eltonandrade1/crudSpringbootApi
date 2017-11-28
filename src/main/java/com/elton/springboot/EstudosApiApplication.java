package com.elton.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.elton.springboot.config.property.PersonalApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(PersonalApiProperty.class)
public class EstudosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstudosApiApplication.class, args);
	}
}
