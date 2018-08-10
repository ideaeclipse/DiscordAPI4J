package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.Objects.SubObjects.DGame;
import DiscordAPI.WebSocket.JsonData.Payloads;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;

public class GameP {
    private DGame game;
    private JSONObject object;

    public GameP(JSONObject object) {
        this.object = object;
    }

    public GameP logic() {
        Payloads.Game g = DiscordUtils.Parser.convertToJSON(object,Payloads.Game.class);
        game = new DGame(g.name, g.state, g.details, g.type);
        return this;
    }

    public DGame getGame() {
        return game;
    }
}
