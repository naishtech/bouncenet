package com.covyne.bouncenet.datastore.mongo;

import com.covyne.bouncenet.core.IEnvironmentVariables;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Value("${com.covyne.bouncenet.mongo.database}")
    private String databaseName;

    @Autowired
    private IEnvironmentVariables environmentVariables;

    @Bean
    public MongoClient mongoClient() {
        final String mongoConnectionURI = environmentVariables.Get("MONGODB_URI", "mongodb://127.0.0.1:27017");
        final ConnectionString connectionString = new ConnectionString(mongoConnectionURI);
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