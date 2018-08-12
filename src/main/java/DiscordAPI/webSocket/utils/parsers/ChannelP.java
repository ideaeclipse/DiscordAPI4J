package DiscordAPI.webSocket.utils.parsers;

import DiscordAPI.objects.DChannel;
import DiscordAPI.webSocket.jsonData.Payloads;
import DiscordAPI.webSocket.utils.DiscordUtils;
import org.json.simple.JSONObject;
import static DiscordAPI.webSocket.utils.DiscordUtils.DefaultLinks.*;

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
        channel = new DChannel(c.id, c.name, c.position, c.nsfw,c.type);
        return this;
    }

    public DChannel getChannel() {
        return channel;
    }
}
