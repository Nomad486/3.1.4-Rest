package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role findById(Long id);
    Role findByName(String name);
    List<Role> findAll();
    Set<Role> findRolesByIds(Set<Long> ids);
}
