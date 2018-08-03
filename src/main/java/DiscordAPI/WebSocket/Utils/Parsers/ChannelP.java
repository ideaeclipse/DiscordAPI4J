package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DChannel;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;

public class ChannelP {
    private Long id;
    private DChannel channel;
    private DiscordBot DiscordBot;
    private JSONObject object;

    public ChannelP(Long id, DiscordBot DiscordBot) {
        this.id = id;
        this.DiscordBot = DiscordBot;
    }

    public ChannelP(JSONObject object) {
        this.object = object;
    }

    public ChannelP logic() {
        if (object == null) {
            object = (JSONObject) DiscordBot.getRequests().get("channels/" + id);
        }
        Payloads.Channel c = DiscordUtils.Parser.convertToJSON(object, Payloads.Channel.class);
        channel = new DChannel(c.id, c.name, c.position, c.nsfw, DiscordBot);
        return this;
    }

    public DChannel getChannel() {
        return channel;
    }
}
