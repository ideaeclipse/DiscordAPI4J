package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.IDiscordBotBuilder;

public class DiscordBotBuilder implements IDiscordBotBuilder {
    private final DiscordBot bot;

    public DiscordBotBuilder(final String token, final Long guildId) {
        bot = new DiscordBot(token, guildId);
    }

    @Override
    public IDiscordBot login() {
        return bot.login();
    }
}
