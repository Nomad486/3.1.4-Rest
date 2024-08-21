package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findByEmail(String email);
    User findById(Long id);
    List<User> findAll();
    void save(User user, Set<Role> roles);
    void update(User user, Set<Role> roles);
    void delete(Long id);
}