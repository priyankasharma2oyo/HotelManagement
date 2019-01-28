package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.entities.User;
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
import java.util.ArrayList;
import java.util.List;

@Service

public class UserService {

    @Qualifier("jestConnection")
    @Autowired
    JestClient client;

    public String addUser(User user){


        Index index=new Index.Builder(user).index("user").type("doc").build();


        try {

            client.execute(index);

            return "New User added";

        }catch(IOException ex){

            return "Exception in adding user "+ex;

        }


    }

    public User getUserById(String userId){



        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("userId", userId ) );
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("user").addType("doc").build();

        User user = new User();

        try {


            SearchResult result = client.execute(search);

            user = result.getSourceAsObject(User.class,false);

        }catch(IOException ex){

            System.out.println("Exception in retrieving state by name "+ex);

        }

        return user;

    }


    public List<User> getAllUsers( ) {



        List<User> users = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        Search search = new Search.Builder(searchSourceBuilder.size(100).toString()).addIndex("user").addType("doc").build();



        try {

            SearchResult result = client.execute(search);

            users = result.getSourceAsObjectList(User.class, false);


        } catch (IOException ex) {

            System.out.println("Exception in retrieving all users "+ex);

        }


        return users;

    }


}
