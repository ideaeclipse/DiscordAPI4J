package ideaeclipse.DiscordAPI.bot.objects.channel;

import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.HttpEvent;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.RequestTypes;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;

import java.util.List;
import java.util.Map;

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
 * @see ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.CreateChannel
 * @see ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.DeleteChannel
 * @see ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.UpdateChannel
 */
public abstract class IChannel {
    private final DiscordBot bot;

    public IChannel(final DiscordBot bot) {
        this.bot = bot;
    }

    /**
     * Allows for sending string based messages in text or direct message channels
     *
     * @param message message wishing to be sent
     * @see ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.Channel
     * @see ideaeclipse.DiscordAPI.bot.objects.channel.directMessage.DMChannel
     */
    public void sendMessage(final String message) {
        if (this.getType() == 0 || this.getType() == 1) {
            Json object = new Json();
            object.put("content", message);
            object.put("file", "file");
            this.bot.getRateLimitRecorder().queue(new HttpEvent(this.bot, RequestTypes.SENDJSON, "channels/" + this.getId() + "/messages", object));
        }
    }

    public void sendEmbed(final List<Field> fields, final String image) {
        if (this.getType() == 0 || this.getType() == 1) {
            Json embed = new Json();
            JsonArray array = new JsonArray();
            for (final Field field : fields) {
                Json temp = new Json();
                temp.put("name", field.getName());
                temp.put("value", field.getValue());
                array.put(temp);
            }
            embed.put("fields", array);
            Json footer = new Json();
            if (image != null && !image.equals("null"))
                footer.put("icon_url", image);
            footer.put("text", "Contact the server's Admins if there is an issue");
            embed.put("footer", footer);
            Json object = new Json();
            object.put("embed", embed);
            this.bot.getRateLimitRecorder().queue(new HttpEvent(this.bot, RequestTypes.SENDJSON, "channels/" + this.getId() + "/messages", object));
        }
    }

    /**
     * TODO: ensure you can't send a file that doesn't exist
     * Allows for sending files in text or direct message channels
     *
     * @param file absolute path to file.
     * @see ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.Channel
     * @see ideaeclipse.DiscordAPI.bot.objects.channel.directMessage.DMChannel
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
            this.bot.getRateLimitRecorder().queue(new HttpEvent(this.bot, RequestTypes.SENDFILE, "channels/" + this.getId() + "/messages", file));
        }
    }

    /**
     * Only for text channels
     *
     * @return nsfw status
     * @see ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.Channel
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
     *
     * @return type of channel 0,1,2,4
     */
    public abstract int getType();

    /**
     * TODO: voice channels @see
     * Only valid for voice channels and direct message channels
     *
     * @return list of participants in the channel
     * @see ideaeclipse.DiscordAPI.bot.objects.channel.directMessage.DMChannel
     */
    public abstract List<IDiscordUser> getReciepients();

    /**
     * @return last 100 messages
     */
    public abstract Map<Long, IMessage> getMessageHistory();

    /**
     * @param message message to add to history
     */
    public abstract void addMessage(final IMessage message);
}
