package com.hotelmanagemetapp.demo.entities;

public class DateWiseRoomAvailability {


    Integer hotelId;
    String date;
    Integer noOfAvailableRooms;

    public Integer getNoOfAvailableRooms() {
        return noOfAvailableRooms;
    }

    public void setNoOfAvailableRooms(Integer noOfAvailableRooms) {
        this.noOfAvailableRooms = noOfAvailableRooms;
    }

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

    @Override
    public String toString() {
        return "DateWiseRoomAvailability{" +
                "hotelId=" + hotelId +
                ", date='" + date + '\'' +
                ", noOfAvailableRooms=" + noOfAvailableRooms +
                '}';
    }

}
