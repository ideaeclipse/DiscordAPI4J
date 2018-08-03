package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DMessage;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelP;
import DiscordAPI.WebSocket.Utils.Parsers.Payloads;
import DiscordAPI.WebSocket.Utils.Parsers.UserP;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Message_Create extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DMessage message;

    public Message_Create(DiscordBot b, JSONObject object) {
        super(b);
        JSONObject d = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(object.get("d")));
        JSONObject user = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(d.get("author")));
        Payloads.User u = DiscordUtils.Parser.convertToJSON(user, Payloads.User.class);
        Payloads.Message m = DiscordUtils.Parser.convertToJSON(d, Payloads.Message.class);
        UserP pd = new UserP(u.id, b).logic();
        ChannelP cd = new ChannelP(m.channel_id, b).logic();
        message = new DMessage(pd.getUser(), cd.getChannel(), m.guild_id, m.content);
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
