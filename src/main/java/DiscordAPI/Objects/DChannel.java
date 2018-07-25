package DiscordAPI.Objects;

import DiscordAPI.Bot.BotImpl;
import org.json.simple.JSONObject;

public class DChannel {
    private Long id;
    private String name;
    private Integer position;
    private Boolean nsfw;
    private BotImpl botImpl;

    public DChannel(Long id, String name, Integer position, Boolean nsfw, BotImpl botImpl) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.nsfw = nsfw;
        this.botImpl = botImpl;
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
        botImpl.getRequests().sendJson("channels/" + id + "/messages",object);
    }
}
