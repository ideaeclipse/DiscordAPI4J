package DiscordBotNew.generic;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.customTerminal.Executable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Commands {
    private final IDiscordBot bot;

    public Commands(final IDiscordBot bot) {
        this.bot = bot;
    }
    @Executable
    public String ping(final String message) {
        if (message.toLowerCase().equals("ping"))
            return "pong";
        return null;
    }
    @Executable
    public String pong(final String message) {
        if (message.toLowerCase().equals("pong"))
            return "ping";
        return null;
    }
}
