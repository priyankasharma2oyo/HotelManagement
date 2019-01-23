package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.utilities.JestConnector;
import com.hotelmanagemetapp.demo.entities.State;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public class StateService {


    @Qualifier("jestConnection")
    @Autowired
    JestClient client;


    public void addState(State state){

        Index index=new Index.Builder(state).index("state").type("doc").build();


        try {

            client.execute(index);

            System.out.println("State added");

        }catch(IOException ex){

            System.out.println("Exception in adding state "+ex);

        }

    }

    public State getState(String stateName){



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("stateName", stateName ) );
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("state").addType("doc").build();

        State state = new State();

        try {


            SearchResult result = client.execute(search);

            state= result.getSourceAsObject(State.class,false);

        }catch(IOException ex){

            System.out.println("Exception in retrieving state by name "+ex);

        }

        return state;

    }

}
