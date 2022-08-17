package com.conyne.bouncenet.hosting;

import java.util.List;

public class ListGamesResponse {

    private List<HostedGame> hostedGameList;

    public ListGamesResponse() {
    }

    public void setHostedGameList(List<HostedGame> hostedGames){
        this.hostedGameList = hostedGames;
    }

    public List<HostedGame> getHostedGameList() {
        return hostedGameList;
    }
}
