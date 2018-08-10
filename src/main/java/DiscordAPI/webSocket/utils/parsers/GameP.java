package DiscordAPI.webSocket.utils.parsers;

import DiscordAPI.objects.subObjects.DGame;
import DiscordAPI.webSocket.jsonData.Payloads;
import DiscordAPI.webSocket.utils.DiscordUtils;
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
