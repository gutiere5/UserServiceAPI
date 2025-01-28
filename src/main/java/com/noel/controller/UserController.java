package com.noel.controller;

import com.noel.model.User;
import com.noel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    /*
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
    */

    @PostMapping
    public User create (@Valid @RequestBody User user) {

        return userService.create(user);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }


}
