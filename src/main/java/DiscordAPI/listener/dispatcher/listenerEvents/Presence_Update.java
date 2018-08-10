package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.objects.DStatus;
import DiscordAPI.webSocket.utils.DiscordLogger;
import DiscordAPI.webSocket.utils.DiscordUtils;
import DiscordAPI.webSocket.utils.parsers.GameP;
import DiscordAPI.webSocket.utils.parsers.UserP;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Presence_Update extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DStatus status;

    public Presence_Update(DiscordBot t, JSONObject payload) {
        super(t);
        JSONObject user = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(payload.get("user")));
        UserP pd = new UserP(Long.parseUnsignedLong(String.valueOf(user.get("id"))), t).logic();
        GameP gd = payload.get("game")!=null?new GameP((JSONObject) payload.get("game")).logic():null;
        status = new DStatus(gd!=null?gd.getGame():null, pd.getUser(), String.valueOf(payload.get("status")));
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
