package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class UpdateChannel extends Event implements IDiscordAction {
    private IChannel channel;

    @Override
    public void initialize(@JsonValidity(value = {"nsfw", "name", "id", "type"}) Json json) {
        CreateChannel channel = new CreateChannel();
        Util.check(channel, json);
        this.channel = channel.getChannel();
    }

    public IChannel getChannel() {
        return channel;
    }
}
