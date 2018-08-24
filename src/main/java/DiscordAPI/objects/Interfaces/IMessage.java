package DiscordAPI.objects.Interfaces;

public interface IMessage {
    IChannel getChannel();

    IDiscordUser getUser();

    Long getGuildId();

    String getContent();
}
