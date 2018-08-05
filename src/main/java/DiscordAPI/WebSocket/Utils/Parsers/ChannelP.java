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

    public ChannelP(Long id) {
        this.id = id;
    }

    public ChannelP(JSONObject object) {
        this.object = object;
    }

    public ChannelP logic() {
        if (object == null) {
            object = (JSONObject) DiscordUtils.HttpRequests.get(DiscordUtils.DefaultLinks.CHANNEL + "/" + id);
        }
        Payloads.Channel c = DiscordUtils.Parser.convertToJSON(object, Payloads.Channel.class);
        channel = new DChannel(c.id, c.name, c.position, c.nsfw);
        return this;
    }

    public DChannel getChannel() {
        return channel;
    }
}
