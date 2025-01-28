package com.noel.service;

import com.noel.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    List<User> getAllUsers();

    User getUser(String userId);
}
