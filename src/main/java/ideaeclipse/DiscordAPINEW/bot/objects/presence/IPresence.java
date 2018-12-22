package ideaeclipse.DiscordAPINEW.bot.objects.presence;

import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

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
    StatusTypes getStatus();

    /**
     * @return user object
     */
    IDiscordUser getUser();

    /**
     * @return game object if not null
     */
    IGame getGame();
}
