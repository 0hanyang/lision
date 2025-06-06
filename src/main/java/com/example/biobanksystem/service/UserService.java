package com.example.biobanksystem.service;

import com.example.biobanksystem.model.Role;
import com.example.biobanksystem.model.User;
import com.example.biobanksystem.repository.RoleRepository;
import com.example.biobanksystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Role createRoleIfNotFound(String name) {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return roleRepository.save(new Role(name));
    }

    @Transactional
    public User createUser(User user, Set<String> roleNames) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username " + user.getUsername() + " already exists.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email " + user.getEmail() + " already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (roleNames == null || roleNames.isEmpty()) {
            // Assign default role if none specified
            roles.add(createRoleIfNotFound("ROLE_USER"));
        } else {
            for (String roleName : roleNames) {
                roles.add(createRoleIfNotFound(roleName));
            }
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Need UserRepository to have these methods:
    // Optional<User> findByUsername(String username);
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);
}
