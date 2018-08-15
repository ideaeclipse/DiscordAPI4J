package DiscordAPI.objects;

/**
 * This class is used for storing Status data
 *
 * @author Ideaeclipse
 */
public class Status {
    private final Game game;
    private final User user;
    private final String status;

    /**
     * @param game   {@link Game}
     * @param user   {@link User}
     * @param status status of game
     */
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
