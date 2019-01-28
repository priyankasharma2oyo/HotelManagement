package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.State;
import com.hotelmanagemetapp.demo.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class StateController {



    // STATE

    @Autowired
    StateService stateService;


    @PostMapping("/addState")
    public String addState(@RequestBody State state){

        return stateService.addState(state);

    }

    @GetMapping("/getAllStates")
    public List<State> getAllStates(){

        return stateService.getAllStates();

    }



}
