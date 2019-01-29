package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.entities.DateWiseRoomAvailability;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class DateWiseRoomAvailabilityService {


    @Qualifier("jestConnection")
    @Autowired
    JestClient client;

    public DateWiseRoomAvailability getByHotelIdAndDate( Integer hotelId, String date ){



        DateWiseRoomAvailability dateWiseRoomAvailability = new DateWiseRoomAvailability();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("hotelId", hotelId)  ;
        QueryBuilder queryBuilder1 = QueryBuilders.matchQuery("date", date)  ;
        QueryBuilder query = QueryBuilders.boolQuery().must(queryBuilder).must(queryBuilder1);
        String searchString = searchSourceBuilder.query(query).toString();

        Search search = new Search.Builder(searchString).addIndex("datewiseroomavailability").addType("doc").build();

        try {

            SearchResult result = client.execute(search);
            dateWiseRoomAvailability = result.getSourceAsObject(DateWiseRoomAvailability.class, false);

        } catch (IOException ex) {

            System.out.println("Exception in retrieving dateWiseRoomAvailability "+ex);

        }

        return dateWiseRoomAvailability;

    }

    public List<DateWiseRoomAvailability> getDateWiseRoomAvailabilityByHotelId( Integer hotelId ){



        List<DateWiseRoomAvailability> dateWiseRoomAvailability = new ArrayList<DateWiseRoomAvailability>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("hotelId", hotelId)  ;
        QueryBuilder query = QueryBuilders.boolQuery().must(queryBuilder);
        String searchString = searchSourceBuilder.query(query).toString();

        Search search = new Search.Builder(searchString).addIndex("datewiseroomavailability").addType("doc").build();

        try {

            SearchResult result = client.execute(search);
            dateWiseRoomAvailability = result.getSourceAsObjectList(DateWiseRoomAvailability.class, false);

        } catch (IOException ex) {

            System.out.println("Exception in retrieving dateWiseRoomAvailability "+ex);

        }

        return dateWiseRoomAvailability;

    }


    public void addDateWiseRoomAvailability(DateWiseRoomAvailability dateWiseRoomAvailability){


        Index index=new Index.Builder(dateWiseRoomAvailability).index("datewiseroomavailability").type("doc").id(Integer.toString(dateWiseRoomAvailability.getHotelId())+dateWiseRoomAvailability.getDate()).build();



        try {

            client.execute(index);

        }catch(IOException ex){

            System.out.println("Exception in adding dateWiseRoomAvailability "+ex);

        }


    }

    public void updateRoomsOnHotelUpdate( Integer hotelId , Integer noOfRooms ){


        List<DateWiseRoomAvailability> dateWiseRoomAvailabilities = new ArrayList<DateWiseRoomAvailability>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("hotelId", hotelId);
        QueryBuilder query = QueryBuilders.boolQuery().must(queryBuilder);
        String searchString = searchSourceBuilder.query(query).toString();

        Search search = new Search.Builder(searchString).addIndex("datewiseroomavailability").addType("doc").build();

        try {

            SearchResult result = client.execute(search);
            dateWiseRoomAvailabilities = result.getSourceAsObjectList(DateWiseRoomAvailability.class, false);

            for(int i = 0 ; i < dateWiseRoomAvailabilities.size() ; i++ ){

                noOfRooms = dateWiseRoomAvailabilities.get(i).getNoOfAvailableRooms()+noOfRooms;
                if(noOfRooms < 0)
                    noOfRooms = 0;

                dateWiseRoomAvailabilities.get(i).setNoOfAvailableRooms(noOfRooms);
                addDateWiseRoomAvailability(dateWiseRoomAvailabilities.get(i));

            }

        } catch (IOException ex) {

            System.out.println("Exception in updating dateWiseRoomAvailability "+ex);

        }

    }



}
