package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.utilities.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class Jobs {

    @Autowired
    BookingService bookingService;

    @Autowired
    RedisService redisService;

    @Scheduled(fixedRate = 5000)
    public void scheduleTaskUsingCronExpression() {

        System.out.println("Redis Job started");

        Map<Integer, ArrayList<Pair>> map = bookingService.getAllTrendingHotelsSetInMap();
        redisService.del("trending");
        redisService.hmset("trending",map );

    }

}
