package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.*;
import com.hotelmanagemetapp.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController


public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    @Autowired
    private DateWiseRoomAvailabilityService dateWiseRoomAvailabilityService;

    // HOTEL


    @PostMapping("/createIndex")
    public void createIndex(@RequestParam("indexName") String indexName ) {

        hotelService.createIndex(indexName);

    }

    @PostMapping("/addHotel")
    public String addHotel(@RequestBody Hotel hotel) {

        return hotelService.addHotel(hotel);

    }


    @GetMapping("/getHotelByIds")
    public List<Hotel> getHotelByIds(@RequestParam("hotelIds") Set<Integer>  hotelIds ) {

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


    @GetMapping("/getHotelByCityNameAndStateName")
    public List<Hotel> getHotelByCityNameAndStateName( @RequestParam("cityName") String cityName , @RequestParam("stateName") String stateName ) {

        List<Hotel> hotels = new ArrayList<Hotel>();

        State state=stateService.getState(stateName);
        if(state!=null) {
            City city = cityService.getCityByState(cityName, state.getStateId());

            if(city!=null)
                hotels = hotelService.getHotelByCityIdAndStateId(city.getCityId(),state.getStateId());
            else
                System.out.println("City not found");

        }else
            System.out.println("State not found");

        return hotels;

    }

    @GetMapping("/getHotelByCityIdAndStateId")
    public List<Hotel> getHotelByCityIdAndStateId( @RequestParam("cityId") Integer cityId , @RequestParam("stateId") Integer stateId ) {

        return hotelService.getHotelByCityIdAndStateId(cityId,stateId);

    }

    @GetMapping("/getHotelByCityId")
    public List<Hotel> getHotelByCityId( @RequestParam("cityId") Integer cityId ) {

        return hotelService.getHotelByCityId(cityId);

    }

    @GetMapping("/getHotelByStateId")
    public List<Hotel> getHotelByStateId( @RequestParam("stateId") Integer stateId ) {

        return hotelService.getHotelByStateId(stateId);

    }


    @GetMapping("/getHotelByStateName")
    public List<Hotel> getHotelByStateName( @RequestParam("stateName") String stateName ) {

        List<Hotel> hotels = new ArrayList<Hotel>();
        State state=stateService.getState(stateName);
        if(state!=null)
            hotels = hotelService.getHotelByStateId(state.getStateId());

        return hotels;

    }


    @GetMapping("/getAllHotels")
    public List<Hotel> getAllHotels(){

        return hotelService.getAllHotels();

    }


    @PostMapping("/deleteHotelById")
    public String deleteHotelById(@RequestParam("hotelId") Integer hotelId){

        return hotelService.deleteHotelById(hotelId);

    }


    @PostMapping("/updateHotel/{hotelId}")
    public String updateHotel(@RequestBody Hotel hotel, @PathVariable("hotelId") Integer hotelId){

        hotel.setHotelId(hotelId);

        if(hotel.getNoOfRooms() != null ){

            Hotel hotel_old = hotelService.getHotelById(hotelId);

            if(hotel_old!=null && hotel_old.getNoOfRooms() != null ) {
                Integer diff = hotel.getNoOfRooms() - hotel_old.getNoOfRooms();
                dateWiseRoomAvailabilityService.updateRoomsOnHotelUpdate(hotelId, diff);
            }

        }

        return hotelService.updateHotel(hotel);

    }


    //date wise






}
