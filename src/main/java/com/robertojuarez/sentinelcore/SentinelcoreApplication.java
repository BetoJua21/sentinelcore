package com.robertojuarez.sentinelcore;

import com.robertojuarez.sentinelcore.repository.UserRepository;
import com.robertojuarez.sentinelcore.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SentinelcoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentinelcoreApplication.class, args);
	}

	@Bean
    CommandLineRunner testDatabaseConnection(UserRepository userRepository) {
		return args -> {
			User testUser = new User("Roberto Juarez", "A1:B2:C3:D4", "ADMIN", true);
			userRepository.save(testUser);
			System.out.println("✅ ¡USUARIO GUARDADO EN MONGODB ATLAS CON ÉXITO!");
		};
	}
}
