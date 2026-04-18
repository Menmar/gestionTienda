package com.menmar.gestionTienda;

import com.menmar.gestionTienda.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableAsync
@EnableScheduling
public class GestionTiendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionTiendaApplication.class, args);
	}

}
