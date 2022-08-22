package com.covyne.bouncenet;

import com.covyne.bouncenet.admin.CreateUserRequest;
import com.covyne.bouncenet.admin.CreateUserResponse;
import com.covyne.bouncenet.admin.GetUserResponse;
import com.covyne.bouncenet.admin.RegisteredUsersController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@TestPropertySource(locations = "classpath:test.properties")
public class RegisteredUsersControllerTests {



    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegisteredUsersController registeredUsersController;

    @Test
    void contextLoads() {
        assertThat(registeredUsersController).isNotNull();
    }

    private static final String testEmail = "test@test.com";
    private static final String testHost = "http://localhost:";

    @LocalServerPort
    private int port;

    @Test
    void manageRegisteredUser() {

        //Setup
        final GetUserResponse response = this.restTemplate
                .getForObject( testHost + port + "/users/" + testEmail, GetUserResponse.class);

        if (response.getEmail() != null) {
            this.restTemplate.delete(testHost + port + "/users/" + testEmail);
        }

        //Test
        final CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setEmail(testEmail);

        final CreateUserResponse createUserResponse = this.restTemplate
                .postForObject("http://localhost:" + port + "/users/create", createUserRequest, CreateUserResponse.class);

        assertThat(createUserResponse).isNotNull();
        assertThat(createUserResponse.getMessage()).isNotNull();
        assertEquals(String.format("User %s created.", testEmail), createUserResponse.getMessage());

        //Cleanup
        this.restTemplate.delete(testHost + port + "/users/" + testEmail);

    }

}
