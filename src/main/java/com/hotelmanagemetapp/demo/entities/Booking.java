package com.hotelmanagemetapp.demo.entities;

import io.searchbox.annotations.JestId;


public class Booking {

    public enum Status{
        Active,Inactive;
    }

    @JestId
    private String bookingId;
    private Integer hotelId;
    private String userId;
    private String checkIn;
    private String checkOut;
    private String date;
    private double amount;
    private Status status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }





    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", hotelId=" + hotelId +
                ", userId='" + userId + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }


}
