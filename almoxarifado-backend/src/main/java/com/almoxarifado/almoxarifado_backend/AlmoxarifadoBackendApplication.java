package com.almoxarifado.almoxarifado_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlmoxarifadoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlmoxarifadoBackendApplication.class, args);
	}
}