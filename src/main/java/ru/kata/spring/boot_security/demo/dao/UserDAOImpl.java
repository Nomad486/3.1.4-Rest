package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    public UserDAOImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmail(String email) {
        return entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public User findByUsername(String username) {
        try {
        return entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.firstName = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    } catch (NoResultException e) {
        return null;
        }
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        User existingUser = entityManager.find(User.class, user.getId());
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setAge(user.getAge());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && user.getPassword().isEmpty()) {
                if (!user.getPassword().equals(user.getPassword())) {
                    if (!user.getPassword().startsWith("$2a$")) {
                        String encodedPassword = passwordEncoder.encode(user.getPassword());
                        existingUser.setPassword(encodedPassword);
                    } else {
                        existingUser.setPassword(user.getPassword());
                    }
                }
            } else {
                existingUser.setPassword(existingUser.getPassword());
            }
            existingUser.setRoles(user.getRoles());
            entityManager.merge(existingUser);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.createQuery("DELETE FROM User u WHERE u.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
