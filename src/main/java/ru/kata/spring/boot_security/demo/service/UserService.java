package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findById(Long id);
    List<User> findAll();
    void save(User user, String roleName);
    void update(User user);
    void delete(Long id);
}