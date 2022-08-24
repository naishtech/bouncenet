package com.covyne.bouncenet.hosting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HostedGamesController {

    @GetMapping("/api/list-games")
    public ListGamesResponse listGames(){

        final ListGamesResponse listGamesResponse = new ListGamesResponse();
        final List<HostedGame> hostedGameList = new ArrayList<>();
        final List<HostedPlayer> playersList = new ArrayList<>();
        final HostedPlayer hostedPlayer = new HostedPlayer();
        final HostedGame hostedGame = new HostedGame();

        hostedPlayer.setPlayerId("Test Player Id");
        playersList.add(hostedPlayer);
        hostedGame.setName("Test Hosted Game Name");
        hostedGame.setAppId("Test App Id");
        hostedGame.setPlayers(playersList);
        hostedGameList.add(hostedGame);
        listGamesResponse.setHostedGameList(hostedGameList);

        return listGamesResponse;
    }

}
