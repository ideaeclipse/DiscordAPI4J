package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

@JsonFormat
/**
 * Converts a json string into an {@link IChannel} object
 * Json example:
 *
 * all keys are matched to exact values in {@link IChannel} and {@link Channel}
 *
 * {
 *   "permission_overwrites": [
 *
 *   ],
 *   "nsfw": false,
 *   "parent_id": null,
 *   "name": "Text Channels",
 *   "guild_id": "471104815192211458",
 *   "position": 0,
 *   "id": "471104815192211459",
 *   "type": 4
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#CHANNEL_CREATE}
 *
 * @author Ideaeclipse
 * @see UpdateChannel
 * @see DeleteChannel
 * @see IChannel
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class CreateChannel extends Event implements IDiscordAction {
    private IChannel channel;

    /**
     * Does a validity check {@link ideaeclipse.DiscordAPINEW.utils.Util#check(EventManager, Event, Json)}
     * and parses that data into a channel object
     *
     * @param json json object received from the websocket
     * @see ideaeclipse.DiscordAPINEW.utils.Util
     */
    @Override
    public void initialize(@JsonValidity( {"nsfw", "name", "id", "type"}) Json json) {
        this.channel = new Channel(Boolean.parseBoolean(String.valueOf(json.get("nsfw")))
                , String.valueOf(json.get("name"))
                , Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , Integer.parseInt(String.valueOf(json.get("type"))));
    }

    /**
     * @return created channel object from {@link #initialize(Json)}
     */
    public IChannel getChannel() {
        return channel;
    }
}
