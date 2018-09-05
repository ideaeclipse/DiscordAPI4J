package DiscordBot.Data.Generic;

import DiscordAPI.IDiscordBot;
import DiscordAPI.Terminal.Terminal;
import DiscordBot.Main;

import java.io.File;

public class defaultCommands {
    private Terminal t;
    private IDiscordBot bot;

    public defaultCommands(Terminal t) {
        this.t = t;
        bot = Main.bot;
    }

    public String getNickName() {
        return "You are: " + t.getUser().getName();
    }

    public String test(String s) {
        return "This is your string: " + s;
    }
}
