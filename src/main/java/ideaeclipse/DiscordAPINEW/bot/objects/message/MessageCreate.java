package ideaeclipse.DiscordAPINEW.bot.objects.message;


import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Map;


public class MessageCreate extends Event implements IDiscordAction {
    private final Map<Long, IChannel> channels;
    private final Map<Long, IDiscordUser> users;
    private IMessage message;

    public MessageCreate(final Map<Long, IChannel> channels, final Map<Long, IDiscordUser> users) {
        this.channels = channels;
        this.users = users;
    }

    @Override
    public void initialize(@JsonValidity(value = {"author", "content", "id", "pinned", "channel_id"}) Json json) {
        this.message = new Message((String) json.get("content")
                , channels.get(Long.parseUnsignedLong(String.valueOf(json.get("channel_id"))))
                , IDiscordUser.parse(Util.check(this, "getUser", new Json(String.valueOf(json.get("author")))).getObject()));
    }

    private IDiscordUser getUser(@JsonValidity(value = {"id"}) Json json) {
        Long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        return users.get(id);
    }

    public IMessage getMessage() {
        return message;
    }
}

