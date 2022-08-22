package com.covyne.bouncenet;

import static org.assertj.core.api.Assertions.assertThat;

import com.covyne.bouncenet.hosting.HostedGamesController;
import com.covyne.bouncenet.hosting.ListGamesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@TestPropertySource(locations = "classpath:test.properties")
class HostedGamesControllerTests {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private HostedGamesController hostedGamesController;

    @Test
    void contextLoads() {
        assertThat(hostedGamesController).isNotNull();
    }

    @Test
    void listGamesControllerReturnsAResponse() {

        ListGamesResponse response = this.restTemplate.getForObject("http://localhost:" + port + "/list-games",
                ListGamesResponse.class);

        assertThat(response).isNotNull();

    }

}
