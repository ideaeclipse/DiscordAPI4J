package DiscordBotNew.generic;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.customTerminal.CommandsClass;
import ideaeclipse.customTerminal.Executable;

public class ChannelCommands implements CommandsClass {
    @Executable
    public String ping(final IDiscordBot bot) {
        return "pong";
    }

    @Executable
    public String pong(final IMessage message) {
        return "ping";
    }

    @Executable
    public String test1(final IMessage message, final String content) {
        return content;
    }

    @Executable
    public String test2(final IDiscordBot bot, final String content) {
        return content;
    }

    @Executable
    public String test3(final IDiscordBot bot, final IMessage message, final String content) {
        return content;
    }

    @Executable
    public String test4(final IMessage message, final IDiscordBot bot, final String content) {
        return content;
    }
}
