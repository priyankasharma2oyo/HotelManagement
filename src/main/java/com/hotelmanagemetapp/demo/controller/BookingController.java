package com.hotelmanagemetapp.demo.controller;

import com.hotelmanagemetapp.demo.entities.DateWiseRoomAvailability;
import com.hotelmanagemetapp.demo.utilities.Pair;
import com.hotelmanagemetapp.demo.entities.Booking;
import com.hotelmanagemetapp.demo.handler.BookingHandler;
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
    public String cancelBooking(@RequestParam("bookingId") String bookingName){


        return bookingHandler.cancelBooking(bookingName);

    }


    @GetMapping("/getAllBookingsByDate")
    public List<Booking> getAllBookingsByDate(@RequestParam("date")String date){

        return bookingHandler.getAllBookingsByDate(date);

    }

    @GetMapping("/getBookingById")
    public Booking getBookingById(@RequestParam("bookingId") String bookingId){
        return bookingHandler.getBookingById(bookingId);
    }

    @GetMapping("/getAllBookingsByHotelIdAndDate")
    public List<Booking> getAllBookingsByHotelIdAndDate(@RequestParam("hotelId") Integer hotelId, @RequestParam("date") String date){

        return bookingHandler.getAllBookingsByHotelIdAndDate(hotelId,date);

    }


    @GetMapping("/getAllBookingsByUserIdAndDate")
    public List<Booking> getAllBookingsByUserIdAndDate(@RequestParam("userId") String userId, @RequestParam("date") String date){

        return bookingHandler.getAllBookingsByUserIdAndDate(userId,date);

    }

    @GetMapping("/getAllBookings")
    public List<Booking> getAllBookings(){

        return bookingHandler.getAllBookings();

    }

    @GetMapping("/getAllTrendingHotelsSetInMap")
    public Map<Integer, ArrayList<Pair>> getAllTrendingHotelsSetInMap(){

        return bookingHandler.getAllTrendingHotelsSetInMap();

    }

    @GetMapping("/getAllTrendingHotelsByCityId")
    public Map<Integer, List<Pair > > getAllTrendingHotelsByCityId(@RequestParam("cityId") Integer cityId){

        return bookingHandler.getAllTrendingHotelsByCityId(cityId);

    }

    @GetMapping("/getAllTrendingHotels")
    public Map<Integer, List<Pair > > getAllTrendingHotels(){

        return bookingHandler.getAllTrendingHotels();

    }




    //Date Wise Room Availability

    @GetMapping("/getNoOfAvailableRoomsByHotelIdAndDate")
    public Integer getNoOfAvailableRoomsByHotelIdAndDate(@RequestParam("hotelId") Integer hotelId, @RequestParam("date") String date ){

        return bookingHandler.getNoOfAvailableRoomsByHotelIdAndDate( hotelId, date );

    }

    @GetMapping("/getDateWiseRoomAvailabilityByHotelId")
    public List<DateWiseRoomAvailability> getDateWiseRoomAvailabilityByHotelId(@RequestParam("hotelId") Integer hotelId){
        return bookingHandler.getDateWiseRoomAvailabilityByHotelId( hotelId);
    }




}
