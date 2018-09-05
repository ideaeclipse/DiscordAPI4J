package DiscordAPI.listener.discordApiListener.listenerTypes;


import DiscordAPI.IPrivateBot;
import DiscordAPI.IDiscordBot;

/**
 * Base Class for listener Library
 * Every Event extends this event
 *
 * @author Ideaeclipse
 */
public abstract class ListenerEvent {
    private IPrivateBot DiscordBot;

    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public ListenerEvent(final IPrivateBot b) {
        this.DiscordBot = b;
    }

    /**
     * @return DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public IDiscordBot getDiscordBot() {
        return this.DiscordBot.getPublicBot();
    }
}
