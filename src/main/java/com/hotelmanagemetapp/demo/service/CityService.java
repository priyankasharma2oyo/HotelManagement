package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.entities.Hotel;
import com.hotelmanagemetapp.demo.utilities.JestConnector;
import com.hotelmanagemetapp.demo.entities.City;
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

public class CityService {


    @Qualifier("jestConnection")
    @Autowired
    JestClient client;

    public void addCity(City city){


        Index index=new Index.Builder(city).index("city").type("doc").build();


        try {

            client.execute(index);

            System.out.println("City added");

        }catch(IOException ex){

            System.out.println("Exception in adding city "+ex);

        }


    }


    public City getCityByState(String cityName, Integer stateId ) {



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQuery= QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("cityName",cityName)).must(QueryBuilders.matchQuery("stateId",stateId));
        searchSourceBuilder.query(matchQuery);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("city").addType("doc").build();

        City city = new City();

        try {

            SearchResult result = client.execute(search);

            city = result.getSourceAsObject(City.class, false);

        } catch (IOException ex) {

            System.out.println("Exception in retrieving city by name "+ex);

        }

        return city;

    }

    public List<String>  getAllCityIds(){

        List<String> list = new ArrayList<String>();



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        Search search = new Search.Builder(searchSourceBuilder.size(100).toString()).addIndex("city").addType("doc").build();

        try {

            List<City> cities = new ArrayList<City>();

            SearchResult result = client.execute(search);

            cities = result.getSourceAsObjectList(City.class, false);

            for(int i=0;i<cities.size();i++){

                list.add(cities.get(i).getCityId().toString());

            }


        } catch (IOException ex) {

            System.out.println("Exception in retrieving all city ids "+ex);

        }

        return list;

    }

}
