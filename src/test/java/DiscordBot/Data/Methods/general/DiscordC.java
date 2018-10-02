package DiscordBot.Data.Methods.general;

import ideaeclipse.DiscordAPI.IDiscordBot;
import DiscordBot.Main;

public class DiscordC {
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
}
