package DiscordAPI.objects.Interfaces;

import DiscordAPI.objects.Payloads;

public interface IChannel {
    Long getId();

    String getName();

    Integer getPosition();

    Boolean getNsfw();

    Payloads.ChannelTypes getType();

    void sendMessage(final String messageContent);
}
