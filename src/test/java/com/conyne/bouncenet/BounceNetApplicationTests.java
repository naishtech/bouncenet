package com.conyne.bouncenet;

import static org.assertj.core.api.Assertions.assertThat;
import com.conyne.bouncenet.hosting.ListGamesController;
import com.conyne.bouncenet.hosting.ListGamesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BounceNetApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private ListGamesController listGamesController;

	@Test
	void contextLoads() {
		assertThat(listGamesController).isNotNull();
	}

	@Test
	void listGamesControllerReturnsAResponse(){

		ListGamesResponse response = this.restTemplate.getForObject("http://localhost:" + port + "/list-games",
				ListGamesResponse.class);

		assertThat(response).isNotNull();

	}

}
