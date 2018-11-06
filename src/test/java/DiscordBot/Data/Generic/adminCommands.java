package DiscordBot.Data.Generic;

import ideaeclipse.DiscordAPI.terminal.CustomTerminal;
import ideaeclipse.DiscordAPI.terminal.Terminal;
import DiscordBot.Main;

public class adminCommands extends CustomTerminal {
    private Terminal t;

    public adminCommands(Terminal t) {
        this.t = t;
    }

    public String getUsers() {
        return String.valueOf(Main.bot.getUsers());
    }

    public String dumpToLogs() {
        return Main.bot.getLoggerManager().dump()? "Info Dumped to logs": "Error dumping files";
    }

    @Override
    public void done() {

    }
}
