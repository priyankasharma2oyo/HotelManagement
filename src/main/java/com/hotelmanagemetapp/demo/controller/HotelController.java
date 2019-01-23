package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.*;
import com.hotelmanagemetapp.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController


public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    // HOTEL


    @PostMapping("/createIndex")
    public void createIndex(@RequestParam("indexName") String indexName ) {

        hotelService.createIndex(indexName);

    }

    @PostMapping("/addHotel")
    public void addHotel(@RequestBody Hotel hotel) {

        hotelService.addHotel(hotel);

    }


    @GetMapping("/getHotelByIds")
    public List<Hotel> getHotelByIds(@RequestParam("hotelIds") List<Integer>  hotelIds ) {

        return  hotelService.getHotelByIds(hotelIds);

    }

    @GetMapping("/getHotelById")
    public Hotel getHotelById(@RequestParam("hotelId") Integer  hotelId ) {

        return hotelService.getHotelById(hotelId);

    }


    @GetMapping("/getHotelByName")
    public List<Hotel> getHotelByName(@RequestParam("hotelName") String  hotelName ) {

        return hotelService.getHotelByName(hotelName);

    }


    @GetMapping("/getHotelByCityAndState")
    public List<Hotel> getHotelByCityAndState( @RequestParam("cityName") String cityName , @RequestParam("stateName") String stateName ) {

        List<Hotel> hotels = new ArrayList<Hotel>();

        State state=stateService.getState(stateName);
        if(state!=null) {
            City city = cityService.getCityByState(cityName, state.getStateId());

            if(city!=null)
                hotels = hotelService.getHotelByCityAndState(city.getCityId(),state.getStateId());
            else
                System.out.println("City not found");

        }else
            System.out.println("State not found");

        return hotels;

    }


    @GetMapping("/deleteHotelById")
    public void deleteHotelById(@RequestParam("hotelId") Integer hotelId){

        hotelService.deleteHotelById(hotelId);

    }


    @PostMapping("/updateHotel")
    public void updateHotel(@RequestBody Hotel hotel){

        hotelService.updateHotel(hotel);

    }


}
