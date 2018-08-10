package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.Objects.DChannel;
import DiscordAPI.WebSocket.JsonData.Payloads;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;
import static DiscordAPI.WebSocket.Utils.DiscordUtils.DefaultLinks.*;

public class ChannelP {
    private Long id;
    private DChannel channel;
    private JSONObject object;

    public ChannelP(Long id) {
        this.id = id;
    }

    public ChannelP(JSONObject object) {
        this.object = object;
    }

    public ChannelP logic() {
        if (object == null) {
            object = (JSONObject) DiscordUtils.HttpRequests.get(CHANNEL + "/" + id);
        }
        Payloads.Channel c = DiscordUtils.Parser.convertToJSON(object, Payloads.Channel.class);
        channel = new DChannel(c.id, c.name, c.position, c.nsfw);
        return this;
    }

    public DChannel getChannel() {
        return channel;
    }
}
