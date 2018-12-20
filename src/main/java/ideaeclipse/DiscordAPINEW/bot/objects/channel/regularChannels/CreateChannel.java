package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class CreateChannel extends Event implements IDiscordAction {
    private IChannel channel;

    @Override
    public void initialize(@JsonValidity(value = {"nsfw", "name", "id", "type"}) Json json) {
        this.channel = new Channel(Boolean.parseBoolean(String.valueOf(json.get("nsfw")))
                , String.valueOf(json.get("name"))
                , Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , Integer.parseInt(String.valueOf(json.get("type"))));
    }

    public IChannel getChannel() {
        return channel;
    }
}
