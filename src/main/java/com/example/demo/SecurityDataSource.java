package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecurityDataSource {
    public static void main(String[] args) {
    }
    @Bean
    public CommandLineRunner run(UserRepository userRepository, RoleRepository rolesRepository) throws Exception{
        return (String[]args)->{
            User user = new User("user", "user@domain", "user", "Bart", "Simpson", true);
            Role userRole = new Role("user", "ROLE_USER");

            userRepository.save(user);
            rolesRepository.save(userRole);

            User admin = new User("admin", "admin@domain", "admin", "Super", "Man", true);
            Role adminRole = new Role("admin", "ROLE_ADMIN");

            userRepository.save(admin);
            rolesRepository.save(adminRole);
        };
    }

}
