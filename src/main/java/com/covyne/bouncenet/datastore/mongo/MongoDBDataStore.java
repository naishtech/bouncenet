package com.covyne.bouncenet.datastore.mongo;

import com.covyne.bouncenet.admin.RegisteredUser;
import com.covyne.bouncenet.core.BeanMapping;
import com.covyne.bouncenet.datastore.IDataStore;
import com.covyne.bouncenet.datastore.mongo.mapping.RegisteredUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MongoDBDataStore implements IDataStore {

    private final Logger logger = LoggerFactory.getLogger(MongoDBDataStore.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BeanMapping beanMapping;

    @Value("${com.covyne.bouncenet.mongo.collection.registeredusers}")
    private String registeredUsersCollectionName;

    public void healthCheck() throws IOException {

        if (mongoTemplate == null) {
            logger.error("Mongo Template is null");
            throw new IOException("Mongo Template is null");
        }
    }

    @Override
    public void createUser(final RegisteredUser registeredUser) {

        RegisteredUserDTO registeredUserDTO = beanMapping.toDTO(registeredUser);
        mongoTemplate.insert(registeredUserDTO, registeredUsersCollectionName);
    }

    @Override
    public void deleteUser(final RegisteredUser registeredUser) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("email").is(registeredUser.getEmail()));
        final List<RegisteredUserDTO> userDTOS = mongoTemplate.find(query, RegisteredUserDTO.class, registeredUsersCollectionName);

        userDTOS.stream()
                .findFirst()
                .ifPresent(registeredUserDTO -> mongoTemplate.remove(registeredUserDTO, registeredUsersCollectionName));

    }

    @Override
    public Optional<RegisteredUser> GetUser(final String email) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        final List<RegisteredUserDTO> userDTOS = mongoTemplate.find(query, RegisteredUserDTO.class, registeredUsersCollectionName);

        return userDTOS.stream()
                .filter(registeredUserDTO -> registeredUserDTO.getEmail().equals(email) )
                .findFirst()
                .map(beanMapping::fromDTO);
    }


}
