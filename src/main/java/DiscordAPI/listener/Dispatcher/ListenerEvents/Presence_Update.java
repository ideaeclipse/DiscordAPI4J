package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DStatus;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import DiscordAPI.WebSocket.Utils.Parsers.GameP;
import DiscordAPI.WebSocket.Utils.Parsers.UserP;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Presence_Update extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DStatus status;

    public Presence_Update(DiscordBot t, JSONObject payload) {
        super(t);
        JSONObject d = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(payload.get("d")));
        JSONObject user = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(d.get("user")));
        UserP pd = new UserP((Long) user.get("id"), t).logic();
        GameP gd = d.get("game")!=null?new GameP((JSONObject) d.get("game")).logic():null;
        status = new DStatus(gd!=null?gd.getGame():null, pd.getUser(), String.valueOf(d.get("status")));
        logger.info("Presence Update: User: " + status.getUser().getName() + " Status: " + status.getStatus() + (status.getGame()!=null?" Game: " + ((status.getGame().getType() == 0) ?
                "Playing " + status.getGame().getName() + " Details: " + status.getGame().getState() + " " + status.getGame().getDetails()
                : "Listening to " + status.getGame().getState() + " Song: " + status.getGame().getDetails() + " on " + status.getGame().getName()):""));
    }

    public DStatus getStatus() {
        return status;
    }

    @Override
    public String getReturn() {
        return "yes";
    }
}
