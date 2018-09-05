package DiscordAPI;

import DiscordAPI.listener.discordApiListener.IDispatcher;
import DiscordAPI.objects.AudioManager;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Interfaces.IRole;
import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.Json;
import DiscordAPI.utils.Properties;

import java.util.List;

public interface IPrivateBot {
    void updateRoles();

    void updateUsers();

    void updateChannels();

    AudioManager getAudioManager();

    List<IChannel> getChannels();

    List<IRole> getRoles();

    List<IUser> getUsers();

    long getGuildId();

    IDiscordBot getPublicBot();

    IDispatcher getDispatcher();

    Json getIdentity();
}
