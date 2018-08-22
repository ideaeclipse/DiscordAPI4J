package DiscordAPI.objects;

/**
 * This class is used for storing Status data
 *
 * @author Ideaeclipse
 */
@Deprecated
public class Status {
    private final Game game;
    private final DiscordUser user;
    private final String status;

    /**
     * @param game   {@link Game}
     * @param user   {@link DiscordUser}
     * @param status status of game
     */
    Status(Game game, DiscordUser user, String status) {
        this.game = game;
        this.user = user;
        this.status = status;
    }

    public DiscordUser getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public String getStatus() {
        return status;
    }
}
