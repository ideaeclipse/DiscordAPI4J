package DiscordBot.Data.Generic;

import DiscordAPI.Terminal.Terminal;

public class defaultCommands {
    private Terminal t;

    public defaultCommands(Terminal t) {
        this.t = t;
    }

    public String whoAmI() {
        return "You are: " + t.getUser();
    }

    public String getCurrentFunction() {
        return "Your current function is: " + t.getCurrentFunction();
    }

    public String showString(String s) {
        return "This is your string: " + s;
    }
}
