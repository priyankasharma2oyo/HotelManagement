package com.hotelmanagemetapp.demo.utilities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)


public class Pair {


    public Integer count,hotelId;


    @JsonCreator
    public Pair( @JsonProperty("count") Integer count, @JsonProperty("hotelId") Integer hotelId) throws IllegalArgumentException {

        if(count == null)
            throw new IllegalArgumentException("Parameter first was not informed.");

        this.count = count;

        this.hotelId = hotelId;

    }

    @Override
    public String toString() {
        return "Pair{" +
                "count=" + count +
                ", hotelId=" + hotelId +
                '}';
    }

}
