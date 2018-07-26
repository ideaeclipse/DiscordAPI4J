package DiscordAPI.Objects;

import DiscordAPI.DiscordBot;
import org.json.simple.JSONObject;

public class DChannel {
    private Long id;
    private String name;
    private Integer position;
    private Boolean nsfw;
    private DiscordBot DiscordBot;

    public DChannel(Long id, String name, Integer position, Boolean nsfw, DiscordBot DiscordBot) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.nsfw = nsfw;
        this.DiscordBot = DiscordBot;
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
        DiscordBot.getRequests().sendJson("channels/" + id + "/messages",object);
    }
}
