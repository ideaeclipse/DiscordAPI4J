package ideaeclipse.DiscordAPI.objects.Interfaces;

import ideaeclipse.DiscordAPI.objects.IDiscordUser;

public interface IMessage {
    IChannel getChannel();

    IDiscordUser getUser();

    Long getGuildId();

    String getContent();
}
