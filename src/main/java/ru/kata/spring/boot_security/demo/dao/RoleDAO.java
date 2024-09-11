package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;
import java.util.Set;

public interface RoleDAO {
    Role findByName(String name);
    Role findById(Long id);
    List<Role> findAll();
    List<Role> findAllByIdIn(Set<Long> ids);
}
