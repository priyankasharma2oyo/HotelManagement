package com.hotelmanagemetapp.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hotelmanagemetapp.demo.utilities.Pair;
import io.lettuce.core.KeyValue;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class RedisService {


    @Qualifier("redisConnection")
    @Autowired
    StatefulRedisClusterConnection<String, String> redisConnection;

    public void set(String key, Object object ) {

        try{

            Gson gson = new Gson();
            redisConnection.sync().set(key,gson.toJson(object));

        } catch (Exception e) {

            System.out.println("Redis set failed for key: " + key + ", exception: ");

        }

    }

    public ArrayList<Pair> get(String key){

        ArrayList<Pair> set = new ArrayList<Pair>();

        try{

            ObjectMapper objectMapper = new ObjectMapper();

            String json = redisConnection.sync().get(key);
            set = objectMapper.readValue(json, new TypeReference<ArrayList<Pair>>(){} );


        } catch (Exception e) {
            System.out.println("Redis set failed for key: " + key + ", exception: "+ e);

        }

        return set;

    }


    public void hmset(String key, Map<Integer, ArrayList<Pair>> fieldMap ) {


        if (CollectionUtils.isEmpty(fieldMap))
            return;

        Gson gson = new Gson();

        try{
            redisConnection.sync().hmset(key, fieldMap.entrySet().stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .collect(Collectors.toMap(e -> e.getKey().toString(), e -> gson.toJson(e.getValue()))));


        } catch (Exception e) {

            System.out.println("Redis hmset failed for key {}, exception: {}  "+ e);

        }

    }

    public void del(String key){

        try {

            redisConnection.sync().del(key);
        }catch(Exception e){

            System.out.println("Redis del failed for key="+key+"  "+ e);

        }
    }


    public Map<Integer, List<Pair > > hgetall(String key) {

         Map<Integer, List<Pair > > finalResponse = new LinkedHashMap<>();

         Gson gson = new Gson();

         try {

            Map< String , String > response = redisConnection.sync().hgetall(key);

            if (!CollectionUtils.isEmpty(response)) {
                for (Map.Entry<String, String> entry : response.entrySet()) {

               //     TypeToken<ArrayList<Pair> > typeToken = new TypeToken<ArrayList<Pair>>() {};
                  //  TypeReference<ArrayList<Pair> > typeReference = new TypeReference<ArrayList<Pair>>() {};

                    finalResponse.put(Integer.parseInt( entry.getKey() ) , gson.fromJson(entry.getValue(), new TypeReference<ArrayList<Pair>>() {}.getType()));

                }
            }


         } catch (Exception e) {
            System.out.println("Redis hmget failed for key: {}, hashFields : {}, exception: {} "+ e);
         }

         return finalResponse;

    }



    public Map<Integer, List<Pair > > hmget(String key,String[] hashfields) {

        Map<Integer, List<Pair > > finalResponse = new LinkedHashMap<>();

        try {

            List< KeyValue<String , String> > response = redisConnection.sync().hmget(key,hashfields);

            for(int i=0;i< response.size(); i++){

               if(response.get(i).hasValue()) {

                    Integer cityId = Integer.parseInt(response.get(i).getKey());

                    ObjectMapper objectMapper = new ObjectMapper();

                    String json = response.get(i).getValue();

                    ArrayList<Pair> list = objectMapper.readValue(json, new TypeReference<ArrayList<Pair>>() {
                    });

                    finalResponse.put(cityId, list);

                }

            }


        } catch (Exception e) {
            System.out.println("Redis hmget failed for key: {}, hashFields : {}, exception: {} "+ e);
        }

        return finalResponse;

    }


//    private void setKeyExpiry(String key, int ttlSeconds) {
//
//        try {
//
//            if (ttlSeconds > 0) {
//                redisConnection.async().expire(key, ttlSeconds);
//            }
//
//        } catch (Exception e) {
//            System.out.println("Exception in redis expiry command for key: " + key + " , exception: "+e);
//        }
//
//    }





}
