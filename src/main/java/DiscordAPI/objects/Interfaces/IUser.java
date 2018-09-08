package DiscordAPI.objects.Interfaces;

import DiscordAPI.objects.IDiscordUser;

import java.util.List;

public interface IUser {
    String getNick();

    String getJoined_at();

    List<IRole> getRoles();

    Boolean getDeaf();

    Boolean getMute();

    String getSession_id();

    IDiscordUser getDiscordUser();

    String getStatus();

    IGame getGame();
}
