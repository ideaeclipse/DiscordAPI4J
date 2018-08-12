package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.objects.DMessage;
import DiscordAPI.webSocket.utils.DiscordLogger;
import DiscordAPI.webSocket.utils.DiscordUtils;
import DiscordAPI.webSocket.utils.parsers.ChannelP;
import DiscordAPI.webSocket.jsonData.Payloads;
import DiscordAPI.webSocket.utils.parsers.UserP;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;
/*
When adding the listener you need to getChannel().getType() and compare it to
DiscordAPI.webSocket.jsonData.Payloads.ChannelTypes
 */
public class Message_Create extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DMessage message;

    public Message_Create(DiscordBot b, JSONObject object) {
        super(b);
        JSONObject user = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(object.get("author")));
        Payloads.User u = DiscordUtils.Parser.convertToJSON(user, Payloads.User.class);
        Payloads.Message m = DiscordUtils.Parser.convertToJSON(object, Payloads.Message.class);
        UserP pd = new UserP(u.id, b).logic();
        ChannelP cd = new ChannelP(m.channel_id).logic();
        message = new DMessage(pd.getUser(), cd.getChannel(), m.guild_id, m.content);
        if(message.getChannel().getType().equals(Payloads.ChannelTypes.textChannel)) {
            logger.info("Message Create: User: " + message.getUser().getName() + " Content: " + message.getContent() + " Channel: " + message.getChannel().getName());
        }else{
            logger.info("Dm Sent: User: " + message.getUser().getName() + " Content: " + message.getContent());
        }
    }


    public DMessage getMessage() {
        return this.message;
    }

    @Override
    public String getReturn() {
        return "";
    }
}
