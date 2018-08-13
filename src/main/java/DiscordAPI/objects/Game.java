package DiscordAPI.objects;

import DiscordAPI.utils.DiscordUtils;
import org.json.simple.JSONObject;

public class Game {
    private final String name;
    private final String state;
    private final String details;
    private final Payloads.GameTypes type;

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

    static class GameP {
        private Game game;
        private final JSONObject object;

        GameP(final JSONObject object) {
            this.object = object;
        }

        GameP logic() {
            Payloads.DGame g = Parser.convertToJSON(object, Payloads.DGame.class);
            game = new Game(g.name, g.state, g.details, g.type);
            return this;
        }

        Game getGame() {
            return game;
        }
    }
}
