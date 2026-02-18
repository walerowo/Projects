package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean

    CommandLineRunner initUsers(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            System.out.println(">>> DATA INITIALIZER <<<");

            if (userRepository.count() > 0) {
                return;
            }

            Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            Role userRole = roleRepository.findByRoleName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            User admin = new User(
                    "Tom",
                    "Adams",
                    37,
                    "tomadams",
                    passwordEncoder.encode("password"),
                    Set.of(adminRole)
            );

            User user = new User(
                    "Lily",
                    "Smith",
                    15,
                    "lilysmith",
                    passwordEncoder.encode("password"),
                    Set.of(userRole)
            );

            userRepository.save(admin);
            userRepository.save(user);
        };
    }
}
