package DiscordAPI.objects;

import DiscordAPI.utils.Json;
import org.json.simple.JSONObject;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.CHANNEL;
import static DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

/**
 * This is the Channel object
 * Has a sub class ChannelP to parse incoming channel json Objects
 *
 * @author Ideaeclipse
 * @see DiscordAPI.objects.Payloads.DChannel
 */
public class Channel {
    private final Long id;
    private final String name;
    private final Integer position;
    private final Boolean nsfw;
    private final Payloads.ChannelTypes type;

    /**
     * Channel is only created using Payloads.DChannel {@link DiscordAPI.objects.Payloads.DChannel}
     *
     * @param id       Channel Id
     * @param name     Channel Name
     * @param position Channel Position in relation to user view on the left handside
     * @param nsfw     is the channel NSFW?
     * @param type     See link for more info {@link DiscordAPI.objects.Payloads.ChannelTypes}
     */
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

    /**
     * @param messageContent String containing the message you wish to send
     */
    public void sendMessage(String messageContent) {
        Json object = new Json();
        messageContent = messageContent.replace("\n", "\\n");
        object.put("content", messageContent);
        object.put("file", "file");
        rateLimitRecorder.queue(new HttpEvent(RequestTypes.sendJson, "channels/" + id + "/messages", object));
    }

    @Override
    public String toString() {
        return "{Channel} Id: " + id + " Name: " + name + " Position: " + position + " Nsfw: " + nsfw + " ChannelType: " + type;
    }

    /**
     * This is a nested class and is used to parse a channel Json
     * This class is only called from {@link Parser} & {@link DiscordBot}
     * must follow new instance with .logic {@link #logic()}
     *
     * @author Ideaeclipse
     */
    static class ChannelP {
        private final Long id;
        private Channel channel;
        private Json object;

        /**
         * When calling this constructor there will be an additional API called to gain the channel Object {@link HttpEvent}
         *
         * @param id Channel Id
         */
        ChannelP(final Long id) {
            this.id = id;
        }

        /**
         * No API call is made because you passed the channel object
         *
         * @param object channel object
         */
        ChannelP(Json object) {
            this.object = object;
            this.id = null;
        }

        /**
         * This is where the Parser parses the data you supplied
         *
         * @return returns the updated ChannelP instance
         * @see DiscordAPI.objects.Payloads.DChannel
         */
        ChannelP logic() {
            if (object == null) {
                object = new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, CHANNEL + "/" + id)));
            }
            Payloads.DChannel c = Parser.convertToPayload(object, Payloads.DChannel.class);
            channel = new Channel(c.id, c.name, c.position, c.nsfw, c.type);
            return this;
        }

        public Channel getChannel() {
            return channel;
        }
    }
}
