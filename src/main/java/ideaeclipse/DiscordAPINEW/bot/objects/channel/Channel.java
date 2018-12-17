package ideaeclipse.DiscordAPINEW.bot.objects.channel;

import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;

import java.io.File;

import static ideaeclipse.DiscordAPINEW.utils.Util.rateLimitRecorder;

public class Channel implements IChannel {
    private final boolean nsfw;
    private final String name;
    private final long id;
    private final int type;

    public Channel(final boolean nsfw, final String name, final long id, final int type) {
        this.nsfw = nsfw;
        this.name = name;
        this.id = id;
        this.type = type;
    }

    @Override
    public boolean isNsfw() {
        return this.nsfw;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void sendMessage(final String message) {
        if(this.type == 0){
            Json object = new Json();
            object.put("content", message);
            object.put("file", "file");
            rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.sendJson, "channels/" + id + "/messages", object));
        }
    }

    @Override
    public void uploadFile(final String file) {
        Json object = new Json();
        object.put("file", "multipart/form-data");
        Json url = new Json();
        url.put("url", "attachment://" + file);
        Json image = new Json();
        image.put("image", url);
        object.put("embed", image);
        rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.sendFile, "channels/" + id + "/messages", file));
    }

    @Override
    public String toString() {
        return "{Channel} Nsfw: " + this.nsfw + " Name: " + this.name + " Id: " + this.id + " Type: " + this.type;
    }
}
