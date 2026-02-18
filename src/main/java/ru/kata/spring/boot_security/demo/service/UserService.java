package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    void addUser(User user, List<String> rolesFromForm);
    void updateUser(User user, List<String> rolesFromForm);
    void deleteUser(int id);
    List<User> getAllUsers();
    User getUserById(int id);
}
