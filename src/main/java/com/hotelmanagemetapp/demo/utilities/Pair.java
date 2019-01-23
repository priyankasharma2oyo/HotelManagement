package com.hotelmanagemetapp.demo.utilities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)


public class Pair {


    public Integer a,b;



   /* public Pair(Integer a,Integer b)
    {
        this.a = a;
        this.b =b;
    }*/

    @JsonCreator
    public Pair( @JsonProperty("a") Integer a, @JsonProperty("b") Integer b) throws IllegalArgumentException {

        if(a == null)
            throw new IllegalArgumentException("Parameter first was not informed.");

        this.a = a;

        this.b = b;

    }



    @Override
    public String toString() {
        return "Pair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

}
