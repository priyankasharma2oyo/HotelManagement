package com.hotelmanagemetapp.demo.entities;

import io.searchbox.annotations.JestId;

public class City {

    @JestId
    Integer cityId;
    Integer stateId;

    String cityName;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    @Override
    public String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", stateId=" + stateId +
                ", cityName='" + cityName + '\'' +
                '}';
    }

}
