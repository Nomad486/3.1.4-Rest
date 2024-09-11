package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
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
    public void save(User user, Set<Role> roles) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        user.setRoles(roles);
        userDAO.save(user);
    }


    @Override
    @Transactional
    public void update(User user, Set<Role> roles) {
        User existingUser = userDAO.findById(user.getId());
        if (existingUser != null) {
            BeanUtils.copyProperties(user, existingUser, "id", "password", "roles");
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    existingUser.setPassword(encodedPassword);
                }
            }
            existingUser.setRoles(roles);
            userDAO.update(existingUser);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userDAO.delete(id);
    }
}
