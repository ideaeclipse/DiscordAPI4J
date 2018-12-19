package ideaeclipse.DiscordAPINEW.bot.objects.presence;


import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

/**
 * @author Ideaeclipse
 * @see IPresence
 * @see PresenceUpdate
 */
public class Presence implements IPresence {
    private final StatusTypes status;
    private final IDiscordUser user;
    private final IGame game;

    Presence(final StatusTypes status, final IDiscordUser user, final IGame game) {
        this.status = status;
        this.user = user;
        this.game = game;
        System.out.println(toString());
    }

    @Override
    public StatusTypes getStatus() {
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
        return "{Presence} Status: " + this.status + " User: " + this.user.getUsername() + (this.game != null ? " Game: " + this.game : "");
    }
}