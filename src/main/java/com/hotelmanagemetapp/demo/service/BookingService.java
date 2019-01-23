package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.utilities.JestConnector;
import com.hotelmanagemetapp.demo.utilities.Pair;
import com.hotelmanagemetapp.demo.entities.Booking;
import com.hotelmanagemetapp.demo.entities.Hotel;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service


public class BookingService {


    @Autowired
    RedisService redisService;

    @Autowired
    HotelService hotelService;

    @Autowired
    CityService cityService;


    @Qualifier("jestConnection")
    @Autowired
    JestClient client;

    public boolean addBooking(Booking booking) {


        Index index = new Index.Builder(booking).index("booking").type("doc").build();


        try {

            client.execute(index);

        //    System.out.println("Booking  done");

            return true;

        } catch (IOException ex) {

            System.out.println("Exception in adding booking"+ex);

            return false;

        }

    }

    public Booking getBookingById(String bookingId) {



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        Booking booking = new Booking();

        searchSourceBuilder.query(QueryBuilders.matchQuery("bookingId", bookingId));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("booking").addType("doc").build();

        try {

            SearchResult result = client.execute(search);

            booking = result.getSourceAsObject(Booking.class, false);

        } catch (IOException ex) {

            System.out.println("Exception in retrieving booking by id "+ex);

        }

        return booking;

    }


    public boolean cancelBooking(Booking booking){


        Index index = new Index.Builder(booking).index("booking").type("doc").build();


        try {

            client.execute(index);

            System.out.println("Cancellation Done");

            return true;

        } catch (IOException ex) {

            System.out.println("Exception in booking cancellation "+ex);

            return false;

        }

    }

    public List<Booking> getAllBookingsByDate(String date){



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("checkIn").lte(date);
        QueryBuilder rangeQuery1= QueryBuilders.rangeQuery("checkOut").gt(date);
        QueryBuilder matchQuery  = QueryBuilders.matchQuery("status", "Active" );

        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(rangeQuery).must(rangeQuery1).must(matchQuery);

        String searchString = searchSourceBuilder.query(queryBuilder).toString();

        Search search = new Search.Builder(searchString).addIndex("booking").addType("doc").build();

        List<Booking> bookings = new ArrayList<>();

        try {

            SearchResult searchResult = client.execute(search);

            bookings = searchResult.getSourceAsObjectList(Booking.class,false);


        }catch(IOException ex){

            System.out.println("Exception in retrieving booking by date  "+ex);

        }

        return bookings;

    }

    public List<Booking> getAllBookingsByHotelId(Integer hotelId , String date){



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("checkIn").lte(date);
        QueryBuilder rangeQuery1= QueryBuilders.rangeQuery("checkOut").gt(date);
        QueryBuilder matchQuery  = QueryBuilders.matchQuery("status", "Active" );
        QueryBuilder matchQuery1 = QueryBuilders.matchQuery("hotelId",hotelId);

        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(rangeQuery).must(rangeQuery1).must(matchQuery).must(matchQuery1);

        String searchString = searchSourceBuilder.query(queryBuilder).toString();

        Search search = new Search.Builder(searchString).addIndex("booking").addType("doc").build();

        List<Booking> bookings = new ArrayList<>();


        try {

            SearchResult searchResult = client.execute(search);

            bookings = searchResult.getSourceAsObjectList(Booking.class,false);


        }catch(IOException ex){

            System.out.println("Exception in retrieving booking by hotel id  on given date "+ex);

        }

        return bookings;

    }


    public List<Booking> getAllBookingsByUserId(String userId , String date){



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("checkIn").lte(date);
        QueryBuilder rangeQuery1= QueryBuilders.rangeQuery("checkOut").gt(date);
        QueryBuilder matchQuery  = QueryBuilders.matchQuery("status", "Active" );
        QueryBuilder matchQuery1 = QueryBuilders.matchQuery("userId",userId);

        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(rangeQuery).must(rangeQuery1).must(matchQuery).must(matchQuery1);

        String searchString = searchSourceBuilder.query(queryBuilder).toString();

        Search search = new Search.Builder(searchString).addIndex("booking").addType("doc").build();

        List<Booking> bookings = new ArrayList<>();


        try {

            SearchResult searchResult = client.execute(search);

            bookings = searchResult.getSourceAsObjectList(Booking.class,false);


        }catch(IOException ex){

            System.out.println("Exception in retrieving booking by user id  "+ex);

        }

        return bookings;

    }


    public Integer getAllBookingsByDateAndHotelId(Integer hotelId , String date){


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        QueryBuilder matchQuery  = QueryBuilders.matchQuery("status", "Active" );
        QueryBuilder matchQuery1 = QueryBuilders.matchQuery("hotelId",hotelId);
        QueryBuilder matchQuery2  = QueryBuilders.matchQuery("date",date );

        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(matchQuery).must(matchQuery1).must(matchQuery2);

        String searchString = searchSourceBuilder.query(queryBuilder).toString();

        Search search = new Search.Builder(searchString).addIndex("booking").addType("doc").build();

        List<Booking> bookings = new ArrayList<>();


        try {

            SearchResult searchResult = client.execute(search);

            bookings = searchResult.getSourceAsObjectList(Booking.class,false);


        }catch(IOException ex){

            System.out.println("Exception in retrieving booking by hotel id on booking date "+ex);

        }

        return bookings.size();

    }




    public Map<Integer, ArrayList<Pair>> getTrendingHotelsSetInMap( ) {




        Map<Integer, ArrayList<Pair>> map = new HashMap<Integer, ArrayList<Pair> >();

        List<Hotel> hotels = hotelService.getHotel();

        LocalDate localDate = LocalDate.now();


        for(int i = 0; i < hotels.size() ; i++ ){

                Integer count = getAllBookingsByDateAndHotelId(hotels.get(i).getHotelId(),localDate.toString());

                Pair p = new Pair(count,hotels.get(i).getHotelId());

                ArrayList<Pair> set;

                if(map.containsKey(hotels.get(i).getCityId())){

                    set = map.get(hotels.get(i).getCityId());

                }else{

                    set = new ArrayList<Pair>();

                }

                set.add(p);

                Collections.sort(set,new Comparator<Pair>() {
                    @Override
                    public int compare(Pair o1, Pair o2) {

                        return o2.a.compareTo(o1.a);

                    }
                });

                if(set.size()>5){
                    set.remove(set.size()-1);
                }

                map.put(hotels.get(i).getCityId(), set );

        }

       Iterator<Map.Entry<Integer, ArrayList<Pair> > > itr = map.entrySet().iterator();

      /*  while(itr.hasNext())
        {
            Map.Entry<Integer, ArrayList<Pair>> entry = itr.next();

            System.out.println("Key = " + entry.getKey() );

            ArrayList<Pair> set= entry.getValue();

            Iterator<Pair> itr1 = set.iterator();

            while(itr1.hasNext()){

                Pair p = itr1.next();

                System.out.println(p.toString());



            }

        }*/


        return map;


    }





    public Map<Integer, List<Pair > > getTrendingHotelsByCityId(Integer cityId){

        String[] s = new String[1];
        s[0]=cityId.toString();

        return redisService.hmget("trending",s);

    }


    public Map<Integer, List<Pair > > getAllTrendingHotels(){

       /* List<String> list = cityService.getAllCityIds();

        String[] s = new String[list.size()];
        list.toArray(s);

        for(int i=0; i< list.size();i++){

            System.out.println(s[i]);

        }*/

        return redisService.hgetall("trending");

    }

}

