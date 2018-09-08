package DiscordAPI.objects.Interfaces;

import DiscordAPI.objects.IDiscordUser;

public interface IMessage {
    IChannel getChannel();

    IDiscordUser getUser();

    Long getGuildId();

    String getContent();
}
