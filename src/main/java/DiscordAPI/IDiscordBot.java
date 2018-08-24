package DiscordAPI;

import DiscordAPI.listener.dispatcher.TDispatcher;
import DiscordAPI.objects.*;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Interfaces.IDiscordUser;
import DiscordAPI.objects.Interfaces.IRole;
import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.Json;

import java.util.List;

public interface IDiscordBot {
    IDiscordBot login();

    long getGuildId();

    //void updateChannels();

    List<IChannel> getChannels();

    List<IRole> getRoles();

    List<IUser> getUsers();

    Json getIdentity();

    TDispatcher getDispatcher();

    //returns bot
    IDiscordUser getBotUser();

    IChannel createDmChannel(IUser user);

    AudioManager getAudioManager();

    void setStatus(Payloads.GameTypes gameType, String gameName);
}
