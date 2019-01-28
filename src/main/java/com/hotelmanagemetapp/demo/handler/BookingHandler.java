package com.hotelmanagemetapp.demo.handler;

import com.hotelmanagemetapp.demo.entities.DateWiseRoomAvailability;
import com.hotelmanagemetapp.demo.entities.User;
import com.hotelmanagemetapp.demo.service.DateWiseRoomAvailabilityService;
import com.hotelmanagemetapp.demo.service.UserService;
import com.hotelmanagemetapp.demo.utilities.Pair;
import com.hotelmanagemetapp.demo.entities.Booking;
import com.hotelmanagemetapp.demo.entities.Hotel;
import com.hotelmanagemetapp.demo.service.BookingService;

import com.hotelmanagemetapp.demo.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component

public class BookingHandler {

    @Autowired
    BookingService bookingService;

    @Autowired
    HotelService hotelService;

    @Autowired
    UserService userService;

    @Autowired
    DateWiseRoomAvailabilityService dateWiseRoomAvailabilityService;

    public String addBooking(Booking booking) {

        if(booking.getUserId()!=null && booking.getHotelId()!=null && booking.getCheckInDate()!=null && booking.getCheckOutDate()!=null) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            String id = "oyo" + booking.getUserId() + dtf.format(now);

            booking.setBookingId(id);

            LocalDate localDate = LocalDate.now();
            booking.setDateOfBooking(localDate.toString());

            LocalDate startDate = LocalDate.parse(booking.getCheckInDate());
            LocalDate endDate = LocalDate.parse(booking.getCheckOutDate());

            Hotel hotel = hotelService.getHotelById(booking.getHotelId());
            User user = userService.getUserById(booking.getUserId());

            if (user != null) {


                if (hotel != null) {

                    if (hotel.getNoOfRooms() != null && hotel.getPrice() != null) {

                        boolean flag = true;

                        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

                            DateWiseRoomAvailability dateWiseRoomAvailability = dateWiseRoomAvailabilityService.getByHotelIdAndDate(booking.getHotelId(), date.toString());

                            if (dateWiseRoomAvailability != null) {

                                if (dateWiseRoomAvailability.getNoOfAvailableRooms() <= 0)
                                    flag = false;

                            } else {

                                if (hotel.getNoOfRooms() <= 0)
                                    flag = false;

                            }

                        }

                        if (!flag)
                            return "Room not available";

                        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

                            DateWiseRoomAvailability dateWiseRoomAvailability = dateWiseRoomAvailabilityService.getByHotelIdAndDate(booking.getHotelId(), date.toString());


                            if (dateWiseRoomAvailability != null) {

                                dateWiseRoomAvailability.setNoOfAvailableRooms(dateWiseRoomAvailability.getNoOfAvailableRooms() - 1);

                                dateWiseRoomAvailabilityService.addDateWiseRoomAvailability(dateWiseRoomAvailability);

                            } else {

                                dateWiseRoomAvailability = new DateWiseRoomAvailability();

                                dateWiseRoomAvailability.setDate(date.toString());
                                dateWiseRoomAvailability.setHotelId(booking.getHotelId());
                                dateWiseRoomAvailability.setNoOfAvailableRooms(hotel.getNoOfRooms() - 1);

                                System.out.println(dateWiseRoomAvailability.toString());

                                dateWiseRoomAvailabilityService.addDateWiseRoomAvailability(dateWiseRoomAvailability);

                            }

                        }

                        long days = ChronoUnit.DAYS.between(startDate, endDate);

                        double amt = days * hotel.getPrice();

                        booking.setAmount(amt);

                        booking.setStatus(Booking.Status.Active);

                        if (bookingService.addBooking(booking)) {

                            return "Booking done successfully with bookingId  " + id;

                        } else
                            return "Booking failed";

                    } else
                        return "no rooms available in this hotel";

                } else
                    return "hotel not found";

            } else
                return "user not found";

        }else
            return "Incomplete Information";


    }


    public String cancelBooking(String bookingId){

            Booking booking = bookingService.getBookingById(bookingId);

            if(booking.getStatus() != Booking.Status.Inactive) {

                LocalDate startDate = LocalDate.parse(booking.getCheckInDate());
                LocalDate endDate = LocalDate.parse(booking.getCheckOutDate());

                for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

                    DateWiseRoomAvailability dateWiseRoomAvailability = dateWiseRoomAvailabilityService.getByHotelIdAndDate( booking.getHotelId(),date.toString() );
                    dateWiseRoomAvailability.setNoOfAvailableRooms(dateWiseRoomAvailability.getNoOfAvailableRooms() + 1);
                    dateWiseRoomAvailabilityService.addDateWiseRoomAvailability(dateWiseRoomAvailability);

                }

                booking.setStatus(Booking.Status.Inactive);

                bookingService.cancelBooking(booking);

                return "Booking cancelled successfully";

            }else
                return "Booking already cancelled";

    }

    public List<Booking> getAllBookingsByDate(String date){

        return bookingService.getAllBookingsByDate(date);


    }

    public Booking getBookingById(String bookingId){
        return bookingService.getBookingById(bookingId);
    }

    public List<Booking> getAllBookingsByHotelIdAndDate(Integer hotelId, String date){

        return bookingService.getAllBookingsByHotelIdAndDate(hotelId,date);


    }

    public List<Booking> getAllBookingsByUserIdAndDate(String hotelId, String date){

        return bookingService.getAllBookingsByUserIdAndDate(hotelId,date);


    }

    public Map<Integer, ArrayList<Pair>> getAllTrendingHotelsSetInMap(){

        return bookingService.getAllTrendingHotelsSetInMap();

    }

    public Map<Integer, List<Pair > > getAllTrendingHotelsByCityId(Integer cityId){

        return bookingService.getAllTrendingHotelsByCityId(cityId);

    }

    public Map<Integer, List<Pair > > getAllTrendingHotels(){

       return bookingService.getAllTrendingHotels();

    }

    public Integer getNoOfAvailableRoomsByHotelIdAndDate( Integer hotelId,  String date ){

        DateWiseRoomAvailability dateWiseRoomAvailability = dateWiseRoomAvailabilityService.getByHotelIdAndDate( hotelId ,date);

        if(dateWiseRoomAvailability !=null)
             return dateWiseRoomAvailability.getNoOfAvailableRooms();
        else{
            Hotel hotel = hotelService.getHotelById(hotelId );
            if(hotel!=null)
                return hotel.getNoOfRooms();
            else
                return 0;

        }

    }

    public List<Booking> getAllBookings(){

        return bookingService.getAllBookings();

    }

}
