package ideaeclipse.DiscordAPINEW.bot.objects.channel;

import java.io.File;

public interface IChannel {
    boolean isNsfw();

    String getName();

    long getId();

    int getType();

    void sendMessage(final String message);

    void uploadFile(final String file);
}
