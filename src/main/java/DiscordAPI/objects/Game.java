package DiscordAPI.objects;

import org.json.simple.JSONObject;

/**
 * Game Object used to parse Game JSONObject's
 *
 * @author Ideaeclipse
 * @see DiscordAPI.objects.Payloads.DGame
 */
public class Game {
    private final String name;
    private final String state;
    private final String details;
    private final Payloads.GameTypes type;

    /**
     * creates new game only called from {@link GameP}
     *
     * @param name    Name of the Game
     * @param state   State of the game
     * @param details Details of the game that is being played
     * @param type    Type {@link DiscordAPI.objects.Payloads.GameTypes}
     */
    private Game(final String name, final String state, final String details, final Payloads.GameTypes type) {
        this.name = name;
        this.state = state;
        this.details = details;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public Payloads.GameTypes getType() {
        return type;
    }

    public String getState() {
        return state;
    }

    /**
     * Parses Game data from a JSONObject
     * {@link #logic()} must be called
     *
     * @author Ideaeclipse
     */
    static class GameP {
        private Game game;
        private final JSONObject object;

        /**
         * @param object Game Object obtains from {@link DiscordAPI.webSocket.Wss}
         */
        GameP(final JSONObject object) {
            this.object = object;
        }

        /**
         * Parses the object and creates a game
         *
         * @return Updated instance of {@link GameP}
         */
        GameP logic() {
            Payloads.DGame g = Parser.convertToPayload(object, Payloads.DGame.class);
            game = new Game(g.name, g.state, g.details, g.type);
            return this;
        }

        Game getGame() {
            return game;
        }
    }
}
