package com.covyne.bouncenet.datastore.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${com.covyne.bouncenet.mongo.database}")
    private String databaseName;

    @Value("${com.covyne.bouncenet.mongo.connectionuri}")
    private String mongoConnectionUri;

    @Bean
    public MongoClient mongoClient() {

        final ConnectionString connectionString = new ConnectionString(mongoConnectionUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), databaseName);
    }
}