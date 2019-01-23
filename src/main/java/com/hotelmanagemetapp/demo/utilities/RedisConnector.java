package com.hotelmanagemetapp.demo.utilities;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hotelmanagemetapp.demo.service.BookingService;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component

@Configuration
public class RedisConnector {

    @Value("${spring.redis.url}")
    private String redisUrl;

    @Bean("redisConnection")
    public StatefulRedisClusterConnection<String, String> createConnection() {


        RedisClusterClient client = RedisClusterClient.create( redisUrl );
        StatefulRedisClusterConnection<String, String> connection = client.connect();
   //     System.out.println("redis connection created");

        return connection;

    }








//    @Override
//    public void destroy() throws Exception {
//
//        client.shutdown();
//    }




}
