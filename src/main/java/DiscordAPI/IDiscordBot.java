package DiscordAPI;

import DiscordAPI.listener.dispatcher.TDispatcher;
import DiscordAPI.objects.*;
import DiscordAPI.utils.Json;

import java.util.List;

public interface IDiscordBot {
    IDiscordBot login();

    long getGuildId();

    void updateChannels();

    List<Channel> getChannels();

    List<Role> getRoles();

    List<User> getUsers();

    Json getIdentity();

    TDispatcher getDispatcher();

    //returns bot
    DiscordUser getBotUser();

    Channel createDmChannel(User user);

    AudioManager getAudioManager();

    void setStatus(Payloads.GameTypes gameType, String gameName);
}
