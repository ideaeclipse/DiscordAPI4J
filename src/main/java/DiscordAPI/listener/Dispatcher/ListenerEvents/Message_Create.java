package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DMessage;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelData;
import DiscordAPI.WebSocket.Utils.Parsers.UserData;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Message_Create extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DMessage message;

    public Message_Create(DiscordBot b, JSONObject object) {
        super(b);
        JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(object.get("d")));
        JSONObject user = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(d.get("author")));
        UserData pd = new UserData(String.valueOf(user.get("id")), b).logic();
        ChannelData cd = new ChannelData((String) d.get("channel_id"), b).logic();
        message = new DMessage(pd.getUser(), cd.getChannel(), Long.parseLong(String.valueOf(d.get("guild_id"))), String.valueOf(d.get("content")));
        logger.info("Message Create: User: " + message.getUser().getName() + " Content: " + message.getContent() + " Channel: " + message.getChannel().getName());
    }

    public DMessage getMessage() {
        return this.message;
    }

    @Override
    public String getReturn() {
        return "";
    }
}
