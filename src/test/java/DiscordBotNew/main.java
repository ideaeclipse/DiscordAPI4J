package DiscordBotNew;

import DiscordBotNew.generic.ChannelCommands;
import ideaeclipse.DiscordAPI.bot.DiscordBotBuilder;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;

public class main {
    public static void main(String[] ars) {
        IDiscordBot bot = new DiscordBotBuilder(ars[0]).setCommandPrefix("!").setCommandChannel("bot").channelCommands(new ChannelCommands()).start();
    }
}
