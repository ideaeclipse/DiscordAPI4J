package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DChannel;
import org.json.simple.JSONObject;

public class ChannelData {
    private String id;
    private DChannel channel;
    private DiscordBot DiscordBot;
    private JSONObject object;
    public ChannelData(String id, DiscordBot DiscordBot) {
        this.id = id;
        this.DiscordBot = DiscordBot;
    }
    public ChannelData(JSONObject object){
        this.object = object;
    }

    public ChannelData logic() {
        if(object==null) {
            object = (JSONObject) DiscordBot.getRequests().get("channels/" + id);
        }
        channel = new DChannel(Long.parseLong(String.valueOf(object.get("id"))), String.valueOf(object.get("name")), Integer.parseInt(String.valueOf(object.get("position"))), Boolean.valueOf(String.valueOf(object.get("nsfw"))), DiscordBot);
        return this;
    }

    public DChannel getChannel() {
        return channel;
    }
}
