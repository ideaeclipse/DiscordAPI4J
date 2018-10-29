package DiscordBot.Data.Generic;

import DiscordBot.Main;
import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.latexConverter.LatexConverter;

import java.io.IOException;

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

    public void sendImage() {
        DiscordUtils.Search.CHANNEL(Main.bot.getChannels(), "bot").sendMessage("Test", "wallpaper.jpg");
    }
    public void equation(String eq) {
        LatexConverter converter = new LatexConverter("latexTemp", "C:\\Program Files\\MikTex 2.9\\miktex\\bin\\x64\\pdflatex.exe");
        eq = eq.replaceAll("\\\\\\\\", "\\\\");
        try {
            DiscordUtils.Search.CHANNEL(Main.bot.getChannels(), "bot").sendMessage("Test", converter.convert(eq));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
