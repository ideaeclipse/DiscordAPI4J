package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.Bot.BotImpl;
import DiscordAPI.Objects.DChannel;
import org.json.simple.JSONObject;

public class ChannelData {
    private String id;
    private DChannel channel;
    private BotImpl botImpl;
    private JSONObject object;
    public ChannelData(String id, BotImpl botImpl) {
        this.id = id;
        this.botImpl = botImpl;
    }
    public ChannelData(JSONObject object){
        this.object = object;
    }

    public ChannelData logic() {
        if(object==null) {
            object = (JSONObject) botImpl.getRequests().get("channels/" + id);
        }
        channel = new DChannel(Long.parseLong(String.valueOf(object.get("id"))), String.valueOf(object.get("name")), Integer.parseInt(String.valueOf(object.get("position"))), Boolean.valueOf(String.valueOf(object.get("nsfw"))), botImpl);
        return this;
    }

    public DChannel getChannel() {
        return channel;
    }
}
