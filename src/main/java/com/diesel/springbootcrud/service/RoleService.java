package com.diesel.springbootcrud.service;

import com.diesel.springbootcrud.entity.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getRole(String role);
}
