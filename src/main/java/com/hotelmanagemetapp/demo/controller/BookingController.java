package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.utilities.Pair;
import com.hotelmanagemetapp.demo.entities.Booking;
import com.hotelmanagemetapp.demo.handler.BookingHandler;
import io.lettuce.core.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController

public class BookingController {

    //BOOKING

    @Autowired
    BookingHandler bookingHandler;

    @PostMapping("/addBooking")
    public String addBooking(@RequestBody Booking booking){

        return bookingHandler.addBooking(booking);



    }

    @PostMapping("/cancelBooking")
    public void cancelBooking(@RequestParam("bookingId") String bookingName){


        bookingHandler.cancelBooking(bookingName);

    }


    @GetMapping("/getAllBookingsByDate")
    public List<Booking> getAllBookingsByDate(@RequestParam("date")String date){

        return bookingHandler.getAllBookingsByDate(date);

    }

    @GetMapping("/getAllBookingsByHotelId")
    public List<Booking> getAllBookingsByHotelId(@RequestParam("hotelId") Integer hotelId, @RequestParam("date") String date){

        return bookingHandler.getAllBookingsByHotelId(hotelId,date);

    }


    @GetMapping("/getAllBookingsByUserId")
    public List<Booking> getAllBookingsByUserId(@RequestParam("userId") String userId, @RequestParam("date") String date){

        return bookingHandler.getAllBookingsByUserId(userId,date);

    }

    @GetMapping("/getTrendingHotelsSetInMap")
    public Map<Integer, ArrayList<Pair>> getTrendingHotelsSetInMap(){

        return bookingHandler.getTrendingHotelsSetInMap();

    }

    @GetMapping("/getTrendingHotelsByCityId")
    public Map<Integer, List<Pair > > getTrendingHotelsByCityId(@RequestParam("cityId") Integer cityId){

        return bookingHandler.getTrendingHotelsByCityId(cityId);

    }

    @GetMapping("/getAllTrendingHotels")
    public Map<Integer, List<Pair > > getAllTrendingHotels(){

        return bookingHandler.getAllTrendingHotels();

    }


    //Booking On date
    @GetMapping("/getAvailableRoomsByHotelId")
    public Integer getAvailableRoomsByHotelId(@RequestParam("hotelId") Integer hotelId, @RequestParam("date") String date ){

        return bookingHandler.getAvailableRoomsByHotelId( hotelId, date );

    }



}
