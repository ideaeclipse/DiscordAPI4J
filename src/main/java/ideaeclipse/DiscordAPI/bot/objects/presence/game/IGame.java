package ideaeclipse.DiscordAPI.bot.objects.presence.game;

/**
 * Object is used to store game information
 *
 * @author ideaeclipse
 * @see Game
 * @see LoadGame
 * @see ideaeclipse.DiscordAPI.bot.objects.presence.PresenceUpdate
 */
public interface IGame {
    /**
     * @return name of game
     */
    String getName();

    /**
     * @return what is happening in the game
     */
    String getState();

    /**
     * @return details on the game
     */
    String getDetails();

    /**
     * @return image details
     */
    String getAdditionalDetails();

    /**
     * @return type of program
     */
    GameType getType();

    /**
     * checks to see if the object you think is a game object can be casted to one.
     * This is to avoid casting issues in {@link LoadGame}
     *
     * @param o theoretical {@link IGame} object
     * @return either a casted instance of object or null
     */
    static IGame parse(final Object o) {
        if (o instanceof IGame) {
            return (IGame) o;
        }
        return null;
    }
}
