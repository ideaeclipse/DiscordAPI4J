package ideaeclipse.DiscordAPINEW.bot.objects.presence.game;

/**
 * A users game status. parsed from a sub json string from a presence update payload
 *
 * @author Ideaeclipse
 * @see IGame
 * @see ideaeclipse.DiscordAPINEW.bot.objects.presence.IPresence
 * @see LoadGame
 * @see ideaeclipse.DiscordAPINEW.bot.objects.presence.PresenceUpdate
 */
public class Game implements IGame {
    private final String name;
    private final String state;
    private final String details;
    private final String additionDetails;
    private final int type;

    /**
     * @param name name of the game
     * @param state state of the game
     * @param details details of the game
     * @param additionalDetails additional details about the game, gathered from the image
     * @param type type of game, 0 is playing, 1 is streaming, 2 is listening
     */
    public Game(final String name, final String state, final String details, final String additionalDetails, final int type) {
        this.name = name;
        this.state = state;
        this.details = details;
        this.additionDetails = additionalDetails;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public String getDetails() {
        return this.details;
    }

    @Override
    public String getAdditionalDetails() {
        return this.additionDetails;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "{Game} Name: " + this.name + " State: " + this.state + " Details: " + this.details + " Additional Info: " + this.additionDetails + " Type: " + this.type;
    }
}
