package DiscordAPI.Objects;

import DiscordAPI.Objects.SubObjects.DGame;

public class DStatus {
    private DGame game;
    private DUser user;
    private String status;

    public DStatus(DGame game, DUser user, String status) {
        this.game = game;
        this.user = user;
        this.status = status;
    }

    public DUser getUser() {
        return user;
    }

    public DGame getGame() {
        return game;
    }

    public String getStatus() {
        return status;
    }
}
