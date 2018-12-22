package ideaeclipse.DiscordAPINEW.bot.objects.channel;

import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;

import java.util.List;

import static ideaeclipse.DiscordAPINEW.utils.Util.rateLimitRecorder;

/**
 * Abstract class that is the super class for all channel objects
 * for examples of each type of channel json payload see the attached class links.
 * <p>
 * Types:
 * 0: text
 * 1: direct message
 * 2: voice
 * 4: channel category
 *
 * @author Ideaeclipse
 * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.CreateChannel
 * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.DeleteChannel
 * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.UpdateChannel
 */
public abstract class IChannel {
    /**
     * Allows for sending string based messages in text or direct message channels
     *
     * @param message message wishing to be sent
     * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.Channel
     * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage.DMChannel
     */
    public void sendMessage(final String message) {
        if (this.getType() == 0 || this.getType() == 1) {
            Json object = new Json();
            object.put("content", message);
            object.put("file", "file");
            rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.sendJson, "channels/" + this.getId() + "/messages", object));
        }
    }

    /**
     * TODO: ensure you can't send a file that doesn't exist
     * Allows for sending files in text or direct message channels
     *
     * @param file absolute path to file.
     * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.Channel
     * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage.DMChannel
     */
    public void uploadFile(final String file) {
        if (this.getType() == 0 || this.getType() == 1) {
            Json object = new Json();
            object.put("file", "multipart/form-data");
            Json url = new Json();
            url.put("url", "attachment://" + file);
            Json image = new Json();
            image.put("image", url);
            object.put("embed", image);
            rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.sendFile, "channels/" + this.getId() + "/messages", file));
        }
    }

    /**
     * Only for text channels
     *
     * @return nsfw status
     * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.Channel
     */
    public abstract boolean isNsfw();

    /**
     * Valid for all channel types
     *
     * @return name of channel
     */
    public abstract String getName();

    /**
     * Valid for all channel types
     *
     * @return id of channel
     */
    public abstract long getId();

    /**
     * Valid for all channel types
     * @return type of channel 0,1,2,4
     */
    public abstract int getType();

    /**
     * TODO: voice channels @see
     * Only valid for voice channels and direct message channels
     * @return list of participants in the channel
     * @see ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage.DMChannel
     */
    public abstract List<IDiscordUser> getReciepients();
}
