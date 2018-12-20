package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Map;

public class DeleteChannel extends Event implements IDiscordAction {
    private final Map<Long, IChannel> channels;

    public DeleteChannel(final Map<Long, IChannel> channels) {
        this.channels = channels;
    }

    @Override
    public void initialize(@JsonValidity(value = "id") Json json) {
        channels.remove(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }
}
