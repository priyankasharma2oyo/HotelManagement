package com.hotelmanagemetapp.demo.handler;

import com.hotelmanagemetapp.demo.utilities.Pair;
import com.hotelmanagemetapp.demo.entities.Booking;
import com.hotelmanagemetapp.demo.entities.Bookingondate;
import com.hotelmanagemetapp.demo.entities.Hotel;
import com.hotelmanagemetapp.demo.service.BookingService;
import com.hotelmanagemetapp.demo.service.BookingondateService;
import com.hotelmanagemetapp.demo.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

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
    BookingondateService bookingondateService;

    public String addBooking(Booking booking) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String id = booking.getUserId() + dtf.format(now) ;

        booking.setBookingId(id);

        LocalDate localDate = LocalDate.now();
        booking.setDate(localDate.toString());

        LocalDate startDate = LocalDate.parse( booking.getCheckIn() );
        LocalDate endDate = LocalDate.parse( booking.getCheckOut() );

        Hotel hotel = hotelService.getHotelById(booking.getHotelId());

        if(hotel!=null) {

            boolean flag = true;

            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

                Bookingondate bookingondate = bookingondateService.getByDateAndHotelId(date.toString(), booking.getHotelId());

                if (bookingondate != null) {

                    if (bookingondate.getAvailableRooms() <= 0)
                        flag = false;

                } else {

                    if (hotel.getAvailableRooms() <= 0)
                        flag = false;

                }

            }

            if (!flag)
                return "Room not available";

            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

                Bookingondate bookingondate = bookingondateService.getByDateAndHotelId(date.toString(), booking.getHotelId());

                if (bookingondate != null) {

                    bookingondate.setAvailableRooms(bookingondate.getAvailableRooms() - 1);

                    bookingondateService.addBookingondate(bookingondate);

                } else {

                    bookingondate = new Bookingondate();

                    bookingondate.setDate(date.toString());
                    bookingondate.setHotelId(booking.getHotelId());
                    bookingondate.setAvailableRooms(hotel.getAvailableRooms() - 1);

                    bookingondateService.addBookingondate(bookingondate);

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

        }else
            return "No such hotel id exists";


    }


    public void cancelBooking(String bookingId){

            Booking booking = bookingService.getBookingById(bookingId);

            if(booking.getStatus() != Booking.Status.Inactive) {

                LocalDate startDate = LocalDate.parse(booking.getCheckIn());
                LocalDate endDate = LocalDate.parse(booking.getCheckOut());

                for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {

                    Bookingondate bookingondate = bookingondateService.getByDateAndHotelId(date.toString(), booking.getHotelId());
                    bookingondate.setAvailableRooms(bookingondate.getAvailableRooms() + 1);
                    bookingondateService.addBookingondate(bookingondate);

                }

                booking.setStatus(Booking.Status.Inactive);

                bookingService.cancelBooking(booking);

            }

    }

    public List<Booking> getAllBookingsByDate(String date){

        return bookingService.getAllBookingsByDate(date);


    }

    public List<Booking> getAllBookingsByHotelId(Integer hotelId, String date){

        return bookingService.getAllBookingsByHotelId(hotelId,date);


    }

    public List<Booking> getAllBookingsByUserId(String hotelId, String date){

        return bookingService.getAllBookingsByUserId(hotelId,date);


    }

    public Map<Integer, ArrayList<Pair>> getTrendingHotelsSetInMap(){

        return bookingService.getTrendingHotelsSetInMap();

    }

    public Map<Integer, List<Pair > > getTrendingHotelsByCityId(Integer cityId){

        return bookingService.getTrendingHotelsByCityId(cityId);

    }

    public Map<Integer, List<Pair > > getAllTrendingHotels(){

       return bookingService.getAllTrendingHotels();

    }

    public Integer getAvailableRoomsByHotelId( Integer hotelId,  String date ){

        Bookingondate bookingondate = bookingondateService.getByDateAndHotelId( date , hotelId );

        if(bookingondate!=null)
             return bookingondate.getAvailableRooms();
        else{
            Hotel hotel = hotelService.getHotelById(hotelId );
            if(hotel!=null)
                return hotel.getAvailableRooms();
            else
                return 0;

        }

    }

}
