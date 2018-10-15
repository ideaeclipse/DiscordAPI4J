package DiscordBot.Data.Generic;

import ideaeclipse.DiscordAPI.Terminal.Terminal;

public class defaultCommands {
    private Terminal t;

    public defaultCommands(Terminal t) {
        this.t = t;
    }

    public String whoAmI() {
        return "You are: " + t.getUser();
    }

    public String getCurrentFunction() {
        if (t.getCurrentFunction() != null)
            return "Your current function is: " + t.getCurrentFunction();
        else
            return "You're not currently attached to a function";
    }

    public String showString(String s) {
        return "This is your string: " + s;
    }
}
