package com.hotelmanagemetapp.demo.utilities;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import org.elasticsearch.Build;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@Component

public class JestConnector {


    @Value("${spring.elasticsearch.jest.uri}")
    private String jestUri;

    @Bean("jestConnection")
    public  JestClient createClient(){


        JestClientFactory factory =new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(jestUri).multiThreaded(true).defaultMaxTotalConnectionPerRoute(2).maxTotalConnection(20).build());

        JestClient client = factory.getObject();

        return client;


    }

}
