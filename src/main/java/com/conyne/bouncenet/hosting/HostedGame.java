package com.conyne.bouncenet.hosting;

import java.util.List;

public class HostedGame {

    private String appId;
    private List<HostedPlayer> players;
    private String name;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<HostedPlayer> getPlayers() {
        return players;
    }
    public void setPlayers(List<HostedPlayer> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
