package DiscordAPI.objects;

public class Status {
    private final Game game;
    private final User user;
    private final String status;

    Status(Game game, User user, String status) {
        this.game = game;
        this.user = user;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public String getStatus() {
        return status;
    }
}
