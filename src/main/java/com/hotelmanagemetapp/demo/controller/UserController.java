package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.User;
import com.hotelmanagemetapp.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    //USER

    @Autowired
    UserService userService;

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user){

        userService.addUser(user);

    }





}
