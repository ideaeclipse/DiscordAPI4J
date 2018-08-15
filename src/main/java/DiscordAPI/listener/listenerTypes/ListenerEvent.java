package DiscordAPI.listener.listenerTypes;


import DiscordAPI.IDiscordBot;

/**
 * Base Class for listener Library
 * Every Event extends this event
 *
 * @author Ideaeclipse
 */
public abstract class ListenerEvent {
    private IDiscordBot DiscordBot;

    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public ListenerEvent(final IDiscordBot b) {
        this.DiscordBot = b;
    }

    /**
     * @return DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public IDiscordBot getDiscordBot() {
        return this.DiscordBot;
    }
}
