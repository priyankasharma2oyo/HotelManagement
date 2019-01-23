package com.hotelmanagemetapp.demo.entities;

public class Bookingondate {


    Integer hotelId;
    String date;
    Integer availableRooms;

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }

    @Override
    public String toString() {
        return "Bookingondate{" +
                "hotelId=" + hotelId +
                ", date='" + date + '\'' +
                ", availableRooms=" + availableRooms +
                '}';
    }


}
