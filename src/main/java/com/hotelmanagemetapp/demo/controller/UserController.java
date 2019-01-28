package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.User;
import com.hotelmanagemetapp.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    //USER

    @Autowired
    UserService userService;

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user){

        return userService.addUser(user);

    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){

        return userService.getAllUsers();

    }



}
