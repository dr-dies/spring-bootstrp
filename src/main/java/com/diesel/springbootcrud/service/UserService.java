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

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //Role userRole = roleRepository.findByRole("ADMIN");
        //user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }
    public void delete(User user) {
        userRepository.delete(user);
    }
    public void edit(User user) {
        userRepository.saveAndFlush(user);
    }
    public User getById(Long id) {
        return userRepository.getById(id);
    }
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}