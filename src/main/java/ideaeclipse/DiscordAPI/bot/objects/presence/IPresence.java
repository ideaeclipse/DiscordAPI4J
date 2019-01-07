package ideaeclipse.DiscordAPI.bot.objects.presence;

import ideaeclipse.DiscordAPI.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;

/**
 * Interface to control access to {@link Presence} object
 *
 * @author Ideaeclipse
 * @see Presence
 * @see PresenceUpdate
 * @see IPresence
 * @see IGame
 */
public interface IPresence {
    /**
     * @return user's status
     */
    UserStatus getStatus();

    /**
     * @return user object
     */
    IDiscordUser getUser();

    /**
     * @return game object if not null
     */
    IGame getGame();
}
