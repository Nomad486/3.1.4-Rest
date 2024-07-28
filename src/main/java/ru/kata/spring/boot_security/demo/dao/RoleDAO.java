package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleDAO {
    Role findByName(String name);
    Role findById(Long id);
    List<Role> findAll();
}
