package DiscordBotNew.dm;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.Field;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.customTerminal.Executable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Commands {
    private final IDiscordBot bot;

    public Commands(final IDiscordBot bot) {
        this.bot = bot;
    }

    @Executable
    public String DmMailCheck(final IMessage message, final String string) {
        return "<N>Info</N>" + "<V>Name: " + message.getUser().getUsername() +
                "\nNickName: " + message.getUser().getNickName() + "\nContent: " + string +
                "</V>";
    }
}
