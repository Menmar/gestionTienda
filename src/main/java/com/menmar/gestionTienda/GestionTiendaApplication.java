package com.menmar.gestionTienda;

import com.menmar.gestionTienda.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class GestionTiendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionTiendaApplication.class, args);
	}

}
