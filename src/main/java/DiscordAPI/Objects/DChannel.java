package DiscordAPI.Objects;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;

public class DChannel {
    private Long id;
    private String name;
    private Integer position;
    private Boolean nsfw;

    public DChannel(Long id, String name, Integer position, Boolean nsfw) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.nsfw = nsfw;
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

    public void sendMessage(String messageContent) {
        JSONObject object = new JSONObject();
        object.put("content", messageContent);
        object.put("file", "file");
        DiscordUtils.HttpRequests.sendJson("channels/" + id + "/messages", object);
    }
}
