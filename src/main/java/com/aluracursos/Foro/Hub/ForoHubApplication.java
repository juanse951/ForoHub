package com.aluracursos.Foro.Hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ForoHubApplication {


	public static void main(String[] args) {
		SpringApplication.run(ForoHubApplication.class, args);
	}

	@Configuration
	public static class MyConfiguration {
		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**")
							.allowedOrigins("https://forohub.up.railway.app")
							.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
							.allowedHeaders("*")
							.allowCredentials(true);
				}
			};
		}
	}
}
