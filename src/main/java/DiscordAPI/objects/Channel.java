package DiscordAPI.objects;

import DiscordAPI.utils.DiscordUtils;
import org.json.simple.JSONObject;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.CHANNEL;
import static DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

public class Channel {
    private final Long id;
    private final String name;
    private final Integer position;
    private final Boolean nsfw;
    private final Payloads.ChannelTypes type;

    private Channel(final Long id, final String name, final Integer position, final Boolean nsfw, final Payloads.ChannelTypes type) {
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

    public void sendMessage(final String messageContent) {
        JSONObject object = new JSONObject();
        object.put("content", messageContent);
        object.put("file", "file");
        rateLimitRecorder.queue(new HttpEvent(RequestTypes.sendJson, "channels/" + id + "/messages", object));
    }

    static class ChannelP {
        private final Long id;
        private Channel channel;
        private JSONObject object;

        ChannelP(final Long id) {
            this.id = id;
        }

        ChannelP(JSONObject object) {
            this.object = object;
            this.id = null;
        }

        ChannelP logic() {
            if (object == null) {
                object = (JSONObject) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, CHANNEL + "/" + id));
            }
            Payloads.DChannel c = Parser.convertToJSON(object, Payloads.DChannel.class);
            channel = new Channel(c.id, c.name, c.position, c.nsfw, c.type);
            return this;
        }

        public Channel getChannel() {
            return channel;
        }
    }
}
