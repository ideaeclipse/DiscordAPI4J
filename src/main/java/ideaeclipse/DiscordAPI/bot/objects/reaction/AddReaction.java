package ideaeclipse.DiscordAPI.bot.objects.reaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import emoji4j.EmojiUtils;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

@JsonFormat
/**
 * This class is used to parse a reaction payload
 * <p>
 * Channel is parsed by {@link IDiscordBot#getChannels()}
 * Message is pared by {@link IChannel#getMessageHistory()}
 * {
 *   "emoji": {
 *     "name": "yeet",
 *     "animated": false,
 *     "id": "504817998087979009"
 *   },
 *   "user_id": "304408618986504195",
 *   "guild_id": "332064083283017728",
 *   "message_id": "533760092282028053",
 *   "channel_id": "531540409097256978"
 * }
 * <p>
 * Called when a payload with the identifier {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#MESSAGE_REACTION_ADD}
 *
 * @author Ideaeclipse
 * @see Reaction
 * @see IReaction
 * @see ideaeclipse.DiscordAPI.bot.DiscordBot
 */
public final class AddReaction extends Event {
    private final IReaction reaction;

    /**
     * Parses the name of the reactions
     * gets channel object form map of channels
     * creates a reaction object
     * adds reaction object to reaction list
     * updates reaction map for message
     *
     * @param json json payload
     * @param bot  bot
     */
    private AddReaction(@JsonValidity({"emoji", "channel_id", "message_id"}) final Json json, final IDiscordBot bot) {
        String s = String.valueOf(Util.check(this, "getEmojiName", new Json(String.valueOf(json.get("emoji")))).getObject());
        IChannel channel = bot.getChannels().getByK1(Long.parseUnsignedLong(String.valueOf(json.get("channel_id"))));
        this.reaction = new Reaction(EmojiUtils.shortCodify(s)
                , s
                , channel
                , channel.getMessageHistory().get(Long.parseUnsignedLong(String.valueOf(json.get("message_id")))));
    }

    /**
     * @param json sub json "emoji"
     * @return json.name
     */
    private String getEmojiName(@JsonValidity("name") final Json json) {
        return String.valueOf(json.get("name"));
    }

    /**
     * @return reaction object
     */
    public IReaction getReaction() {
        return reaction;
    }
}
