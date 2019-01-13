package ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

@JsonFormat
/**
 * Validates json string. and uses {@link CreateChannel#initialize(Json)} to parse the data
 * and returns a valid {@link IChannel} object. The old channel object will then be replaced with the new one
 *
 *
 * Json Example:
 *
 * {
 *   "permission_overwrites": [
 *     {
 *       "allow": 0,
 *       "deny": 1,
 *       "id": "471104815192211458",
 *       "type": "role"
 *     }
 *   ],
 *   "last_message_id": null,
 *   "nsfw": false,
 *   "rate_limit_per_user": 0,
 *   "parent_id": "495015318138388530",
 *   "name": "test",
 *   "guild_id": "471104815192211458",
 *   "topic": null,
 *   "position": 3,
 *   "id": "525121830827196438",
 *   "type": 0
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#CHANNEL_UPDATE}
 *
 * @author Ideaeclipse
 * @see CreateChannel
 * @see DeleteChannel
 * @see IChannel
 * @see ideaeclipse.DiscordAPI.webSocket.Wss#Wss(IDiscordBot, String)
 */
public final class UpdateChannel extends Event {
    private final IDiscordBot bot;
    private final IChannel channel;

    /**
     * validates json string and passes it to {@link CreateChannel}
     *
     * @param json json string received from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
     * @param bot  passed from Util
     */
    private UpdateChannel(@JsonValidity({"nsfw", "name", "id", "type"}) final Json json, final IDiscordBot bot) {
        this.bot = bot;
        CreateChannel channel = Util.checkConstructor(CreateChannel.class, json, this.bot).getObject();
        this.channel = channel.getChannel();
    }

    /**
     * @return created channel object from {@link CreateChannel}
     */
    public IChannel getChannel() {
        return channel;
    }
}
