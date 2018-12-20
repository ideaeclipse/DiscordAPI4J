package ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.reflectionListener.Event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateDMChannel extends Event implements IDiscordAction {
    private final Map<Long, IDiscordUser> users;
    private final Map<Long, IChannel> channels;

    public CreateDMChannel(final Map<Long, IDiscordUser> users, final Map<Long, IChannel> channels) {
        this.users = users;
        this.channels = channels;
    }

    @Override
    public void initialize(@JsonValidity(value = {"recipients", "id", "type"}) Json json) {
        List<IDiscordUser> recipients = new LinkedList<>();
        for (Json json1 : new JsonArray(String.valueOf(json.get("recipients")))) {
            Util.check(this, "getId", json1).ifPresent(o -> recipients.add(IDiscordUser.parse(o)));
        }
        this.channels.put(Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , new DMChannel(Long.parseUnsignedLong(String.valueOf(json.get("id")))
                        , Integer.parseInt(String.valueOf(json.get("type")))
                        , recipients
                ));
    }

    public IDiscordUser getId(@JsonValidity(value = "id") Json json) {
        return users.get(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }
}
