package com.hotelmanagemetapp.demo.service;

import com.hotelmanagemetapp.demo.utilities.JestConnector;
import com.hotelmanagemetapp.demo.entities.User;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public class UserService {

    @Qualifier("jestConnection")
    @Autowired
    JestClient client;

    public void addUser(User user){


        Index index=new Index.Builder(user).index("user").type("doc").build();


        try {

            client.execute(index);

            System.out.println("New User added");

        }catch(IOException ex){

            System.out.println("Exception in adding user "+ex);

        }


    }

}
