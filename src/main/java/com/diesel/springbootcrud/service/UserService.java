package com.diesel.springbootcrud.service;

import com.diesel.springbootcrud.entity.Role;
import com.diesel.springbootcrud.entity.User;
import com.diesel.springbootcrud.repository.RoleRepository;
import com.diesel.springbootcrud.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail (String email) {
        return userRepository.findByEmail(email);
    }

    public User add(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public void delete(User user) {
        userRepository.delete(user);
    }
    public void update(User user) {
        userRepository.saveAndFlush(user);
    }
    public User getById(Long id) {
        return userRepository.getById(id);
    }
    public List<User> allUsers() {
        return userRepository.findAll();
    }


    public Set<Role> getRole(String role) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRole(role));
        return roles;
    }

}