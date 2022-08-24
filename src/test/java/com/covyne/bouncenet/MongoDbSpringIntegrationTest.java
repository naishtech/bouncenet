package com.covyne.bouncenet;

import com.covyne.bouncenet.admin.RegisteredUser;
import com.covyne.bouncenet.datastore.mongo.MongoDBDataStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class MongoDbSpringIntegrationTest {


    @DisplayName("given object to save"
            + " when save object using MongoDB template"
            + " then object is saved")
    @Test
    public void mongoDBSavesAndDeletesRegisteredUser(@Autowired MongoDBDataStore mongoDBDataStore) {

        final RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setEmail("test@test.com");

        final Optional<RegisteredUser> existingUser = mongoDBDataStore.GetUser("test@test.com");

        if (existingUser.isPresent()) {
            mongoDBDataStore.deleteUser(registeredUser);
        }

        mongoDBDataStore.createUser(registeredUser);

        final Optional<RegisteredUser> createdUser = mongoDBDataStore.GetUser("test@test.com");

        if (createdUser.isEmpty()) {
            fail("test user could not be created.");
        }

        mongoDBDataStore.deleteUser(registeredUser);

        final Optional<RegisteredUser> deletedUser = mongoDBDataStore.GetUser("test@test.com");

        if (deletedUser.isPresent()) {
            fail("test user was not deleted.");
        }

    }
}
