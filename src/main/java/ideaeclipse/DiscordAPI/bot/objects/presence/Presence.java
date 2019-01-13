package ideaeclipse.DiscordAPI.bot.objects.presence;


import ideaeclipse.DiscordAPI.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;

/**
 * Mapped Presence data to presence object
 *
 * @author Ideaeclipse
 * @see IPresence
 * @see PresenceUpdate
 * @see UserStatus
 * @see IGame
 */
public final class Presence implements IPresence {
    private final UserStatus status;
    private final IDiscordUser user;
    private final IGame game;

    /**
     * @param status users status
     * @param user   user object
     * @param game   game objecty
     */
    Presence(final UserStatus status, final IDiscordUser user, final IGame game) {
        this.status = status;
        this.user = user;
        this.game = game;
    }

    @Override
    public UserStatus getStatus() {
        return this.status;
    }

    @Override
    public IDiscordUser getUser() {
        return this.user;
    }

    @Override
    public IGame getGame() {
        return this.game;
    }

    @Override
    public String toString() {
        return "{Presence} Status: " + this.status + (this.user != null ? " User: " + this.user.getUsername() : "") + (this.game != null ? " Game: " + this.game : "");
    }
}
