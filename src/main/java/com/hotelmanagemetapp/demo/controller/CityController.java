package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.City;
import com.hotelmanagemetapp.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class CityController {


    // CITY

    @Autowired
    CityService cityService;

    @PostMapping("/addCity")
    public void addCity(@RequestBody City city){

        cityService.addCity(city);

    }



}
