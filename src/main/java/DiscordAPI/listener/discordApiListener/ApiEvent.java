package DiscordAPI.listener.discordApiListener;


import DiscordAPI.IPrivateBot;
import DiscordAPI.IDiscordBot;

/**
 * Base Class for listener Library
 * Every Event extends this event
 *
 * @author Ideaeclipse
 */
public abstract class ApiEvent {
    private IPrivateBot DiscordBot;

    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public ApiEvent(final IPrivateBot b) {
        this.DiscordBot = b;
    }

    /**
     * @return DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public IDiscordBot getDiscordBot() {
        return this.DiscordBot.getPublicBot();
    }
}