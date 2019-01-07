package DiscordBotNew;

import ideaeclipse.DiscordAPI.bot.DiscordBot;

public class main {
    public static DiscordBot bot;
    public static void main(String[] ars) {
        bot = new DiscordBot(ars[0], ars[1]);
    }
}
