package DiscordBot.Data.Methods.included;

import ideaeclipse.DiscordAPI.IDiscordBot;
import DiscordBot.Main;
import ideaeclipse.DiscordAPI.terminal.CustomTerminal;

public class DiscordC extends CustomTerminal {
    private IDiscordBot client;

    public DiscordC() {
        client = Main.bot;
    }

    public String getChannels() {
        return String.valueOf(client.getChannels());
    }

    public String getUsers() {
        return String.valueOf(client.getUsers());
    }

    public String getRoles() {
        return String.valueOf(client.getRoles());
    }

    @Override
    public void done() {

    }
}
