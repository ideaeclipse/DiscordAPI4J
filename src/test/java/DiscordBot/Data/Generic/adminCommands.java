package DiscordBot.Data.Generic;

import DiscordAPI.Terminal.Terminal;
import DiscordBot.Main;

public class adminCommands {
    private Terminal t;

    public adminCommands(Terminal t) {
        this.t = t;
    }

    public String getUsers() {
        return String.valueOf(Main.bot.getUsers());
    }

}
