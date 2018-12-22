package ideaeclipse.DiscordAPINEW.bot.objects.message;


import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

import java.util.Map;

@JsonFormat
/**
 * TODO: add json example
 * This class parses a Create message payload and creates a {@link IMessage} object
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#MESSAGE_CREATE}
 *
 * @author Ideaeclipse
 * @see IMessage
 * @see Message
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class MessageCreate extends Event implements IDiscordAction {
    private final Map<Long, IChannel> channels;
    private final Map<Long, IDiscordUser> users;
    private IMessage message;

    /**
     * @param channels channel map
     * @param users    users map
     */
    public MessageCreate(final Map<Long, IChannel> channels, final Map<Long, IDiscordUser> users) {
        this.channels = channels;
        this.users = users;
    }

    /**
     * {@link Util#check(EventManager, Event, Json)} ensures json string has valid content
     *
     * @param json json string delivered from the websocket
     */
    @Override
    public void initialize(@JsonValidity( {"author", "content", "id", "pinned", "channel_id"}) Json json) {
        this.message = new Message((String) json.get("content")
                , channels.get(Long.parseUnsignedLong(String.valueOf(json.get("channel_id"))))
                , IDiscordUser.parse(Util.check(this, "getUser", new Json(String.valueOf(json.get("author")))).getObject()));
    }

    /**
     * Parses sub json string author for the id of the user and then searches the user map for a user
     *
     * @param json sub json string author
     * @return Author's id value from user map
     */
    private IDiscordUser getUser(@JsonValidity( {"id"}) Json json) {
        Long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        return users.get(id);
    }

    /**
     * @return Message object
     */
    public IMessage getMessage() {
        return message;
    }
}

