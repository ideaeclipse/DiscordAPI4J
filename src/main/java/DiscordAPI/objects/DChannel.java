package DiscordAPI.objects;

import DiscordAPI.webSocket.jsonData.Payloads;
import DiscordAPI.webSocket.utils.DiscordUtils;
import org.json.simple.JSONObject;

public class DChannel {
    private Long id;
    private String name;
    private Integer position;
    private Boolean nsfw;
    private Payloads.ChannelTypes type;

    public DChannel(Long id, String name, Integer position, Boolean nsfw, Payloads.ChannelTypes type) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.nsfw = nsfw;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPosition() {
        return position;
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public Payloads.ChannelTypes getType() {
        return type;
    }

    public void sendMessage(String messageContent) {
        JSONObject object = new JSONObject();
        object.put("content", messageContent);
        object.put("file", "file");
        DiscordUtils.HttpRequests.sendJson("channels/" + id + "/messages", object);
    }
}
