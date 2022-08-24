package com.covyne.bouncenet.datastore.mongo;
import com.covyne.bouncenet.admin.RegisteredUser;
import com.covyne.bouncenet.core.BeanMapping;
import com.covyne.bouncenet.datastore.IDataStore;
import com.covyne.bouncenet.datastore.mongo.mapping.HealthCheckDTO;
import com.covyne.bouncenet.datastore.mongo.mapping.RegisteredUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@ApplicationScope
public class MongoDBDataStore implements IDataStore {

    private final Logger logger = LoggerFactory.getLogger(MongoDBDataStore.class);

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BeanMapping beanMapping;

    @Value("${com.covyne.bouncenet.mongo.collection.registeredusers}")
    private String registeredUsersCollectionName;

    @Value("${com.covyne.bouncenet.mongo.collection.healthCheck}")
    private String healthCheckCollectionName;

    public void healthCheck() throws IOException {

        if (mongoTemplate == null) {
            logger.error("Mongo Template is null");
            throw new IOException("Mongo Template is null");
        }

        final String appId = this.buildProperties.getName();
        final String appVersion = this.buildProperties.getVersion();

        //clear out existing checks
        final Query query = new Query();
        query.addCriteria(Criteria.where("serviceName").is(appId));
        final List<HealthCheckDTO> existingHealthCheckDTOs = mongoTemplate.find(query, HealthCheckDTO.class, healthCheckCollectionName);
        existingHealthCheckDTOs.forEach(doc -> mongoTemplate.remove(doc, healthCheckCollectionName));

        //create new check
        final HealthCheckDTO healthCheckDTO = new HealthCheckDTO();
        final String checkTime = Instant.now().toString();
        healthCheckDTO.setServiceName(appId);
        healthCheckDTO.setServiceVersion(appVersion);
        healthCheckDTO.setLastStatusCheckTime(checkTime);
        mongoTemplate.insert(healthCheckDTO, healthCheckCollectionName);

        //retrieve new check
        final List<HealthCheckDTO> newHealthCheckDTOs = mongoTemplate.find(query, HealthCheckDTO.class, healthCheckCollectionName);
        if(newHealthCheckDTOs.stream().noneMatch(dto -> dto.getServiceName().equals(appId) && dto.getLastStatusCheckTime().equals(checkTime))){
            throw new IOException("MongoDB Health Check failed.");
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
