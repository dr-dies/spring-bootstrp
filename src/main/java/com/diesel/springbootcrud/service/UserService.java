package com.diesel.springbootcrud.service;

import com.diesel.springbootcrud.entity.Role;
import com.diesel.springbootcrud.entity.User;
import com.diesel.springbootcrud.repository.RoleRepository;
import com.diesel.springbootcrud.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
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
        List<Role> roles = roleRepository.findAll();
        if(role.equals(roles.get(0).getRole())) {
            roles.remove(1);
        } else {
            roles.remove(0);
        }
        return new HashSet<Role>(roles);
    }

}