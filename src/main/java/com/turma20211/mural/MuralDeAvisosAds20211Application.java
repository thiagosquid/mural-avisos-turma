package com.turma20211.mural;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Mural da Turma",
                description = "Api para Sistema de Fórum, Comunicação e Estudos do IFS",
                version = "0.0.1",
                contact = @Contact(name = "Equipe Mural da Turma", url = "muraldaturma.com.br", email = "muraldaturma@gmail.com.br")
        ))
public class MuralDeAvisosAds20211Application {

    public static void main(String[] args) {
        SpringApplication.run(MuralDeAvisosAds20211Application.class, args);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
