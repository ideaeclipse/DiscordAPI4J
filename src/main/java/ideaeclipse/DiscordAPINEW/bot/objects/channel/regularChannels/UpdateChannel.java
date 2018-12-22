package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
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
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#CHANNEL_UPDATE}
 *
 * @author Ideaeclipse
 * @see CreateChannel
 * @see DeleteChannel
 * @see IChannel
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class UpdateChannel extends Event implements IDiscordAction {
    private IChannel channel;

    /**
     * validates json string and passes it to {@link CreateChannel}
     * @param json json string received from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     */
    @Override
    public void initialize(@JsonValidity( {"nsfw", "name", "id", "type"}) Json json) {
        CreateChannel channel = new CreateChannel();
        Util.check(channel, json);
        this.channel = channel.getChannel();
    }

    /**
     * @return created channel object from {@link CreateChannel}
     */
    public IChannel getChannel() {
        return channel;
    }
}
