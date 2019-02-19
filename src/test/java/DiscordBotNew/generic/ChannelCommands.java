package DiscordBotNew.generic;

import ideaeclipse.customTerminal.CommandsClass;
import ideaeclipse.customTerminal.Executable;

public class ChannelCommands implements CommandsClass {
    @Executable
    public String ping(){
        return "pong";
    }
    @Executable
    public String pong(){
        return "ping";
    }
}
