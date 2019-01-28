package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.City;
import com.hotelmanagemetapp.demo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class CityController {


    // CITY

    @Autowired
    CityService cityService;

    @PostMapping("/addCity")
    public String addCity(@RequestBody City city){

        return cityService.addCity(city);

    }

    @GetMapping("/getAllCities")
    public List<City> getAllCities(){

        return cityService.getAllCities();

    }



}
