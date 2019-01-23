package ideaeclipse.DiscordAPI.bot.objects.message;


import com.fasterxml.jackson.annotation.JsonFormat;
import emoji4j.EmojiUtils;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.reflectionListener.Event;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@JsonFormat
/**
 * This class parses a Create message payload and creates a {@link IMessage} object
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#MESSAGE_CREATE}
 *
 * {
 *   "mention_everyone": false,
 *   "pinned": false,
 *   "attachments": [
 *
 *   ],
 *   "author": {
 *     "id": "178616639049170945",
 *     "avatar": "a_33974a329c457906fb68a9bdff12bc5f",
 *     "username": "Minghao",
 *     "discriminator": "6666"
 *   },
 *   "mention_roles": [
 *
 *   ],
 *   "type": 0,
 *   "edited_timestamp": "2018-08-31T01:37:52.828000+00:00",
 *   "content": "rule #1: We only talk about mcdonald",
 *   "tts": false,
 *   "mentions": [
 *
 *   ],
 *   "id": "484899446019325967",
 *   "embeds": [
 *
 *   ],
 *   "channel_id": "484899360971161607",
 *   "timestamp": "2018-08-31T01:37:16.927000+00:00"
 * }
 *
 * @author Ideaeclipse
 * @see IMessage
 * @see Message
 * @see ideaeclipse.DiscordAPI.webSocket.Wss#Wss(IDiscordBot, String)
 */
@SuppressWarnings("ALl")
public final class MessageCreate extends Event {
    private final IDiscordBot bot;
    private final IMessage message;

    /**
     * {@link Util#checkConstructor(Class, Json, IDiscordBot)} ensures json string has valid content
     *
     * @param json json string delivered from the websocket
     */
    private MessageCreate(@JsonValidity({"author", "content", "id", "pinned", "channel_id"}) Json json, final IDiscordBot bot) {
        this.bot = bot;
        Object o = Util.check(this, "getReactions", json).getObject();
        Map<String, Integer> reactionMap;
        if (o instanceof Map)
            reactionMap = (Map<String, Integer>) o;
        else
            reactionMap = new HashMap<>();
        this.message = new Message(Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , String.valueOf(json.get("content"))
                , this.bot.getChannels().getByK1(Long.parseUnsignedLong(String.valueOf(json.get("channel_id"))))
                , IDiscordUser.parse(Util.check(this, "getUser", new Json(String.valueOf(json.get("author")))).getObject())
                , reactionMap);
        if (this.message.getChannel() != null)
            this.message.getChannel().addMessage(this.message);
    }

    /**
     * Parses sub json string author for the id of the user and then searches the user map for a user
     *
     * @param json sub json string author
     * @return Author's id value from user map
     */
    private IDiscordUser getUser(@JsonValidity({"id"}) Json json) {
        return this.bot.getUsers().getByK1(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }

    /**
     * parses reactions for loading messages
     *
     * @param json json string
     * @return map of reactions
     */
    private Map<String, Integer> getReactions(@JsonValidity("reactions") final Json json) {
        Map<String, Integer> map = new HashMap<>();
        for (Object object : new JsonArray(String.valueOf(json.get("reactions")))) {
            Json emoji = new Json(String.valueOf(object));
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) Util.check(this, "getIndividualReaction", emoji).getObject();
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private Map.Entry<String, Integer> getIndividualReaction(@JsonValidity({"emoji", "count"}) final Json json) {
        String name = String.valueOf(Util.check(this, "getEmojiName", new Json(String.valueOf(json.get("emoji")))).getObject());
        int count = Integer.parseInt(String.valueOf(json.get("count")));
        return new AbstractMap.SimpleEntry<>(EmojiUtils.shortCodify(name), count);
    }

    private String getEmojiName(@JsonValidity("name") final Json json) {
        return String.valueOf(json.get("name"));
    }

    /**
     * @return Message object
     */
    public IMessage getMessage() {
        return message;
    }
}

