package com.example.biobanksystem.config;

import com.example.biobanksystem.model.User;
import com.example.biobanksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.createRoleIfNotFound("ROLE_USER");
        userService.createRoleIfNotFound("ROLE_ADMIN");

        // Create an admin user if not exists
        if (userService.findUserByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("adminpassword"); // Raw password, will be encoded by UserService
            adminUser.setEmail("admin@example.com");

            Set<String> adminRoles = new HashSet<>();
            adminRoles.add("ROLE_ADMIN");
            adminRoles.add("ROLE_USER");

            userService.createUser(adminUser, adminRoles);
            System.out.println("Admin user created: admin");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
