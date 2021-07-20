package com.diesel.springbootcrud.service;

import com.diesel.springbootcrud.entity.Role;
import com.diesel.springbootcrud.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> getRole(String role) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRole(role));
        return roles;
    }
}
