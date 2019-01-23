package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.State;
import com.hotelmanagemetapp.demo.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class StateController {



    // STATE

    @Autowired
    StateService stateService;


    @PostMapping("/addState")
    public void addState(@RequestBody State state){

        stateService.addState(state);

    }



}
