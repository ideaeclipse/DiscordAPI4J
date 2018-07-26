package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.Objects.SubObjects.DGame;
import org.json.simple.JSONObject;

public class GameData {
    private DGame game;
    private JSONObject object;

    public GameData(JSONObject object) {
        this.object = object;
    }

    public GameData logic() {
        game = new DGame(String.valueOf(object.get("name")), String.valueOf(object.get("state")), String.valueOf(object.get("details")), Integer.parseInt(String.valueOf(object.get("type"))));
        return this;
    }

    public DGame getGame() {
        return game;
    }
}
