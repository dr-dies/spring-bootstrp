package com.diesel.springbootcrud.service;

import com.diesel.springbootcrud.entity.User;

import java.util.List;

public interface UserService {
    User findUserByEmail(String email);
    User add(User user);
    void delete(User user);
    void update(User user);
    User getById(Long id);
    List<User> allUsers();
}
