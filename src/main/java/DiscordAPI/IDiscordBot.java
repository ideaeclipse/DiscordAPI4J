package DiscordAPI;

import DiscordAPI.listener.dispatcher.TDispatcher;
import DiscordAPI.objects.Channel;
import DiscordAPI.objects.Payloads;
import DiscordAPI.objects.Role;
import DiscordAPI.objects.User;
import org.json.simple.JSONObject;

import java.util.List;

public interface IDiscordBot {
    IDiscordBot login();

    long getGuildId();

    void updateChannels();

    List<Channel> getChannels();

    List<Role> getRoles();

    List<User> getUsers();

    JSONObject getIdentity();

    TDispatcher getDispatcher();

    //returns bot
    User getUser();

    Channel createDmChannel(User user);

    void setStatus(Payloads.GameTypes gameType, String gameName);
}
