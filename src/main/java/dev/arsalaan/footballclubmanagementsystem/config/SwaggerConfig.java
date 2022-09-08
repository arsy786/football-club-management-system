package dev.arsalaan.footballclubmanagementsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;



@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot Football Club Management System REST API",
                version = "1.0",
                description = "My API Description",
                license = @License(name = "License of API", url = "http://APIlicense.url"),
                contact = @Contact(url = "www.arsalaan.dev", name = "Arsalaan", email = "arsalaan@gmail.dev")
        )
)
public class SwaggerConfig {

}
