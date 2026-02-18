package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    @Override
    public void addUser(User user, List<String> rolesFromForm) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();

        for (String roleName : rolesFromForm) {
            Role role = roleRepository.findByRoleName(roleName)
                            .orElseThrow(() -> new IllegalArgumentException("Role is not found: " + roleName)
                            );
            roles.add(role);

        }

        user.setRoles(roles);
        userRepository.save(user);
    }

//    @Transactional
//    @Override
//    public void updateUser(User user) {
//        User existingUser = userRepository.findById(user.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        if (!user.getPassword().equals(existingUser.getPassword())) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//        }
//
//        userRepository.save(user);
//    }

    @Transactional
    @Override
    public void updateUser(User user, List<String> rolesFromForm) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(user.getName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setUsername(user.getUsername());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

//        if (rolesFromForm != null) {
//            Set<Role> roles = new HashSet<>();
//            for (String roleName : rolesFromForm) {
//                Role role = roleRepository.findByRoleName(roleName)
//                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
//                roles.add(role);
//            }
//            existingUser.setRoles(roles);
//        }

        userRepository.save(existingUser);
    }


    @Transactional
    @Override
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoles().clear();
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User is not found"));
    }

}
