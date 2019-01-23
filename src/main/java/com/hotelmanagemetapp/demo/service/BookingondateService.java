package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.utilities.JestConnector;
import com.hotelmanagemetapp.demo.entities.Bookingondate;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BookingondateService {

        //Logger log = LoggerFactory.getLogger(BookingondateService.class);

        @Qualifier("jestConnection")
        @Autowired
        JestClient client;

        public Bookingondate getByDateAndHotelId(String date, Integer hotelId){



            Bookingondate bookingondate = new Bookingondate();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.matchQuery("hotelId", hotelId)  ;
            QueryBuilder queryBuilder1 = QueryBuilders.matchQuery("date", date)  ;
            QueryBuilder query = QueryBuilders.boolQuery().must(queryBuilder).must(queryBuilder1);
            String searchString = searchSourceBuilder.query(query).toString();

            Search search = new Search.Builder(searchString).addIndex("bookingondate").addType("doc").build();

            try {

                SearchResult result = client.execute(search);
                bookingondate = result.getSourceAsObject(Bookingondate.class, false);

            } catch (IOException ex) {

                System.out.println("Exception in retrieving bookingondate "+ex);

            }

            return bookingondate;

        }


    public void addBookingondate(Bookingondate bookingondate){


        Index index=new Index.Builder(bookingondate).index("bookingondate").type("doc").id(Integer.toString(bookingondate.getHotelId())+bookingondate.getDate()).build();



        try {

            client.execute(index);

       //     System.out.println("Bookingondate added");

        }catch(IOException ex){

            System.out.println("Exception in adding bookingondate "+ex);

        }


    }






}
