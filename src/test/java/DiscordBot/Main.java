package DiscordBot;

import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.DiscordAPI.objects.DiscordBotBuilder;

public class Main {
    public static IDiscordBot bot;

    private Main(String token, Long guildId) {
        bot = new DiscordBotBuilder(token, new EventClass(), guildId).login();
    }

    public static void main(String[] args) {
        new Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}
