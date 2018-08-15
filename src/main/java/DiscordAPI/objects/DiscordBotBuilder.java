package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.IDiscordBotBuilder;

/**
 * Object is using to create a new discordBot instance
 * must call .login()
 *
 * @author Ideaeclipse
 */
public class DiscordBotBuilder implements IDiscordBotBuilder {
    private final DiscordBot bot;

    /**
     * @param token   bot token
     * @param guildId guildId
     */
    public DiscordBotBuilder(final String token, final Long guildId) {
        bot = new DiscordBot(token, guildId);
    }

    /**
     * @return updated Instance of IDiscordBot
     */
    @Override
    public IDiscordBot login() {
        return bot.login();
    }
}
