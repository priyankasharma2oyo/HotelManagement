package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.entities.Hotel;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service

public class HotelService {


    @Qualifier("jestConnection")
    @Autowired
    JestClient client;

    public void createIndex(String indexName){

        try {

            client.execute(new CreateIndex.Builder(indexName).build());


        }catch(IOException ex){

            System.out.println("Exception in creating index "+ex);

        }


    }

    public String addHotel(Hotel hotel){

        if(hotel.getHotelId()!=null) {

            Index index = new Index.Builder(hotel).index("hotel").type("doc").build();

            try {

                client.execute(index);

                return "Hotel created successfully";

            } catch (IOException ex) {

                return "Exception in creating hotel " + ex;

            }

        }else
            return "hotel id not present";

    }

    public List<Hotel> getHotelByIds(Set<Integer>  hotelIds ) {


        List<Hotel> hotels = new ArrayList<Hotel>();


        for ( Integer hotelId: hotelIds) {

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            searchSourceBuilder.query(QueryBuilders.matchQuery("hotelId", hotelId));

            Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("hotel").addType("doc").build();



            try {

                SearchResult result = client.execute(search);

                Hotel hotel = result.getSourceAsObject(Hotel.class, false);

                if(hotel!=null) {
                    hotels.add(hotel);

                }

            } catch (IOException ex) {

                System.out.println("Exception in retrieving hotel by ids "+ex);

            }

        }

        return hotels;

    }

    public List<Hotel> getAllHotels( ) {



        List<Hotel> hotels = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        Search search = new Search.Builder(searchSourceBuilder.size(1000).toString()).addIndex("hotel").addType("doc").build();



        try {

            SearchResult result = client.execute(search);

            hotels = result.getSourceAsObjectList(Hotel.class, false);


        } catch (IOException ex) {

            System.out.println("Exception in retrieving all hotels "+ex);

        }


        return hotels;

    }



    public Hotel getHotelById(Integer  hotelId ) {



        Hotel hotel = new Hotel();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("hotelId", hotelId));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("hotel").addType("doc").build();

        try {

            SearchResult result = client.execute(search);

            hotel = result.getSourceAsObject(Hotel.class, false);



        } catch (IOException ex) {

                System.out.println("Exception in retrieving hotel by id "+ex);

        }

        return hotel;

    }

    public List<Hotel> getHotelByName(String  hotelName ) {


        List<Hotel> hotels = new ArrayList<Hotel>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQuery = QueryBuilders.matchQuery("hotelName",hotelName).fuzziness("AUTO");
        QueryBuilder prefix = QueryBuilders.prefixQuery("hotelName",hotelName);
        searchSourceBuilder.query(QueryBuilders.boolQuery().should(matchQuery).should(prefix) );
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("hotel").addType("doc").build();


        try {

            SearchResult result = client.execute(search);

            hotels = result.getSourceAsObjectList(Hotel.class, false);

        } catch (IOException ex) {

            System.out.println("Exception in retrieving hotel by name "+ex);

        }

        return hotels;

    }

    public List<Hotel> getHotelByCityIdAndStateId( Integer cityId , Integer stateId ) {



        List<Hotel> hotels = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQuery=QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("cityId",cityId)).must(QueryBuilders.matchQuery("stateId",stateId));
        searchSourceBuilder.query(matchQuery);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("hotel").addType("doc").build();

        try {

            SearchResult result = client.execute(search);

            hotels = result.getSourceAsObjectList(Hotel.class, false);


        } catch (IOException ex) {

            System.out.println("Exception in retrieving hotel by city and state "+ex);

        }

        return hotels;

    }

    public List<Hotel> getHotelByCityId( Integer cityId ) {



        List<Hotel> hotels = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQuery=QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("cityId",cityId));
        searchSourceBuilder.query(matchQuery);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("hotel").addType("doc").build();

        try {

            SearchResult result = client.execute(search);

            hotels = result.getSourceAsObjectList(Hotel.class, false);


        } catch (IOException ex) {

            System.out.println("Exception in retrieving hotel by city id "+ex);

        }

        return hotels;

    }

    public List<Hotel> getHotelByStateId( Integer stateId ) {



        List<Hotel> hotels = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQuery=QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("stateId",stateId));
        searchSourceBuilder.query(matchQuery);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("hotel").addType("doc").build();

        try {

            SearchResult result = client.execute(search);

            hotels = result.getSourceAsObjectList(Hotel.class, false);


        } catch (IOException ex) {

            System.out.println("Exception in retrieving hotel by state id "+ex);

        }

        return hotels;

    }



    public String deleteHotelById(Integer hotelId){


        Delete delete = new Delete.Builder(Integer.toString(hotelId)).index("hotel").type("doc").build();

        try {

            client.execute(delete);

            return "Hotel deleted with id = "+hotelId;


        } catch (IOException ex) {

            return "Exception in deleting hotel by id "+ex;

        }

    }



    private enum UpdatePayload {
        DOC("doc"),
        DOC_AS_UPSERT("doc_as_upsert");

        private final String key;

        UpdatePayload(String key) {
            this.key = key;
        }

    }


    private Map<String, Object> getUpdatePayload(Hotel data, boolean upsert) {

        Map<String, Object> payload = new HashMap<>();
        payload.put(UpdatePayload.DOC.key, data);
        payload.put(UpdatePayload.DOC_AS_UPSERT.key, upsert);
        return payload;

    }

    public String updateHotel(Hotel hotel){

        Update updateAction = new Update.Builder(getUpdatePayload(hotel, false))
                .index("hotel")
                .type("doc")
                .id(Integer.toString(hotel.getHotelId()))
                .build();



        try {

            DocumentResult res = client.execute(updateAction);

            if(res.isSucceeded())
                return "hotel updated successfully";
            else
                return "hotel not found";


        } catch (IOException ex) {

            return "Exception in updating hotel "+ex;

        }

    }

}
