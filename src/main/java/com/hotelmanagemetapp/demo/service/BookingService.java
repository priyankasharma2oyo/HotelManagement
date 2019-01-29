package com.hotelmanagemetapp.demo.service;

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

        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("checkInDate").lte(date);
        QueryBuilder rangeQuery1= QueryBuilders.rangeQuery("checkOutDate").gt(date);
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

    public List<Booking> getAllBookingsByHotelIdAndDate(Integer hotelId , String date){



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("checkInDate").lte(date);
        QueryBuilder rangeQuery1= QueryBuilders.rangeQuery("checkOutDate").gt(date);
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

            System.out.println("Exception in retrieving booking by hotel id and date "+ex);

        }

        return bookings;

    }


    public List<Booking> getAllBookingsByUserIdAndDate(String userId , String date){



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("checkInDate").lte(date);
        QueryBuilder rangeQuery1= QueryBuilders.rangeQuery("checkOutDate").gt(date);
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

            System.out.println("Exception in retrieving booking by user id and date "+ex);

        }

        return bookings;

    }


    public Integer getAllBookingsByHotelIdAndDateOfBooking(Integer hotelId , String date){


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        QueryBuilder matchQuery  = QueryBuilders.matchQuery("status", "Active" );
        QueryBuilder matchQuery1 = QueryBuilders.matchQuery("hotelId",hotelId);
        QueryBuilder matchQuery2  = QueryBuilders.matchQuery("dateOfBooking",date );

        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(matchQuery).must(matchQuery1).must(matchQuery2);

        String searchString = searchSourceBuilder.query(queryBuilder).toString();

        Search search = new Search.Builder(searchString).addIndex("booking").addType("doc").build();

        List<Booking> bookings = new ArrayList<>();


        try {

            SearchResult searchResult = client.execute(search);

            bookings = searchResult.getSourceAsObjectList(Booking.class,false);


        }catch(IOException ex){

            System.out.println("Exception in retrieving bookings by hotel id on booking date "+ex);

        }

        return bookings.size();

    }




    public Map<Integer, ArrayList<Pair>> getAllTrendingHotelsSetInMap( ) {




        Map<Integer, ArrayList<Pair>> map = new HashMap<Integer, ArrayList<Pair> >();

        List<Hotel> hotels = hotelService.getAllHotels();

        LocalDate localDate = LocalDate.now();


        for(int i = 0; i < hotels.size() ; i++ ){

                if(hotels.get(i).getCityId()!=null) {

                    Integer count = getAllBookingsByHotelIdAndDateOfBooking(hotels.get(i).getHotelId(), localDate.toString());

                    if(count!=0) {

                        Pair p = new Pair(count, hotels.get(i).getHotelId());

                        ArrayList<Pair> set;

                        if (map.containsKey(hotels.get(i).getCityId())) {

                            set = map.get(hotels.get(i).getCityId());

                        } else {

                            set = new ArrayList<Pair>();

                        }

                        set.add(p);

                        Collections.sort(set, new Comparator<Pair>() {
                            @Override
                            public int compare(Pair o1, Pair o2) {

                                return o2.count.compareTo(o1.count);

                            }
                        });

                        if (set.size() > 5) {
                            set.remove(set.size() - 1);
                        }

                        map.put(hotels.get(i).getCityId(), set);

                    }

                }

        }

        return map;


    }





    public Map<Integer, List<Pair > > getAllTrendingHotelsByCityId(Integer cityId){

        String[] s = new String[1];
        s[0]=cityId.toString();

        return redisService.hmget("trending",s);

    }


    public Map<Integer, List<Pair > > getAllTrendingHotels(){

        return redisService.hgetall("trending");

    }

    public List<Booking> getAllBookings( ) {



        List<Booking> bookings = new ArrayList<Booking>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        Search search = new Search.Builder(searchSourceBuilder.size(100).toString()).addIndex("booking").addType("doc").build();



        try {

            SearchResult result = client.execute(search);

            bookings = result.getSourceAsObjectList(Booking.class, false);


        } catch (IOException ex) {

            System.out.println("Exception in retrieving all bookings "+ex);

        }


        return bookings;

    }


}

