package ideaeclipse.DiscordAPI.objects.Interfaces;

import ideaeclipse.DiscordAPI.objects.Payloads;

import java.util.List;

public interface IChannel {
    Long getId();

    String getName();

    Integer getPosition();

    Boolean getNsfw();

    Payloads.ChannelTypes getType();

    List<IMessage> messageHistory();

    void sendMessage(final String messageContent);

    void sendMessage(final String messageContent, String file);
}

