package ru.kata.spring.boot_security.demo.configs;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final RoleService roleService;

    public StringToRoleConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role convert(String source) {
        Long id = Long.valueOf(source);
        Role role = roleService.findById(id);
        return role != null ? role : null;
    }
}