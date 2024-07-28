package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.roleDAO = roleDAO;
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    @Transactional
    public void save(User user, String roleName) {
        Role role = roleDAO.findByName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Role not found: " + roleName);
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        System.out.println("Encoded password: " + encodedPassword);
        user.setPassword(encodedPassword);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(role));
        userDAO.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userDAO.delete(id);
    }
}
