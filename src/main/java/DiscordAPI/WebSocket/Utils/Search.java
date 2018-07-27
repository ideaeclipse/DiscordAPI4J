package DiscordAPI.WebSocket.Utils;

import DiscordAPI.Objects.DChannel;
import DiscordAPI.Objects.DRole;

import java.util.List;
public class Search {
    public static DChannel CHANNEL(List<DChannel> channels, String channelName){
        for(DChannel channel: channels){
            if(channel.getName().toLowerCase().equals(channelName.toLowerCase())){
                return channel;
            }
        }
        return null;
    }
    public static DRole ROLES(List<DRole> roles, Long id){
        for(DRole role:roles){
            if(role.getId().equals(id)){
                return role;
            }
        }
        return null;
    }
}
