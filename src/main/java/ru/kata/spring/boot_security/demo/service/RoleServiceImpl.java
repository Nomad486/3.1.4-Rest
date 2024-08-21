package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;

    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public Role findById(Long id) {
        return roleDAO.findById(id);
    }

    @Override
    public Role findByName(String name) {
        return roleDAO.findByName(name);
    }

    @Override
    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    @Override
    public Set<Role> findRolesByIds(Set<Long> ids) {
        return new HashSet<>(roleDAO.findAllByIdIn(ids));
    }
}
