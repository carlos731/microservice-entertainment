package com.microservice.entertainment;

import com.microservice.entertainment.config.ConfigProperties;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "EntertainmentService REST API Documentation",
				description = "EntertainmentService REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Carlos Henrique",
						email = "carlos@gmail.com",
						url = "https://www.entertainment.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.entertainment.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "EntertainmentService REST API Documentation",
				url = "http://localhost:8080/swagger-ui.html"
		)
)
@SecurityScheme(
		name = "Bearer Authentication",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
@EnableConfigurationProperties(value = {ConfigProperties.class})
public class EntertainmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntertainmentApplication.class, args);
	}

}
