package DiscordBotNew.dm;

import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.customTerminal.CommandsClass;
import ideaeclipse.customTerminal.Executable;

public class DmCommands implements CommandsClass {

    @Executable
    public String DmMailCheck(final IMessage message, final String string) {
        return "<N>Info</N>" + "<V>Name: " + message.getUser().getUsername() +
                "\nNickName: " + message.getUser().getNickName() + "\nContent: " + string +
                "</V>";
    }
}
