package ideaeclipse.DiscordAPI.objects;

import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.reflectionListener.Listener;

/**
 * Object is using to create a new discordBot instance
 * must call .login()
 *
 * @author Ideaeclipse
 */
public class DiscordBotBuilder{
    private final DiscordBot bot;

    /**
     * @param token   bot token
     * @param guildId guildId
     */
    public DiscordBotBuilder(final String token, final Listener listener,final Long guildId) {
        bot = new DiscordBot(token, listener, guildId);
    }

    /**
     * @return updated Instance of IDiscordBot
     */
    public IDiscordBot login() {
        return bot.login();
    }
}
