package ru.kata.spring.boot_security.demo.dao;



import org.springframework.data.jpa.repository.EntityGraph;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserDAO {
    User findByUsername(String username);
    User findById(Long id);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(Long id);
}
