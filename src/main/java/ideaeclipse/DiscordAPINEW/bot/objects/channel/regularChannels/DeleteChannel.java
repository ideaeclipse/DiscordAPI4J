package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Map;

@JsonFormat
/**
 * Remove's a channel based on it's id
 *
 * Json example
 *
 * Only finds id of channel.
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
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#CHANNEL_DELETE}
 *
 * @author Ideaeclipse
 * @see CreateChannel
 * @see UpdateChannel
 * @see IChannel
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class DeleteChannel extends Event {
    private final IPrivateBot bot;
    private final IChannel channel;

    /**
     * Ensures key value id is present and removes based on id.
     * Id will always be a valid long.
     *
     * @param json json string sent from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     * @param bot  bot from util
     */
    private DeleteChannel(@JsonValidity("id") Json json, final IPrivateBot bot) {
        this.bot = bot;
        long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        this.channel = this.bot.getChannels().get(id);
        this.bot.getChannels().remove(id);
    }

    /**
     * @return deleted channel object
     */
    public IChannel getChannel() {
        return channel;
    }
}
