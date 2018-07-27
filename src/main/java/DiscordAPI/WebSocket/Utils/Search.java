package DiscordAPI.WebSocket.Utils;

import DiscordAPI.Objects.DChannel;

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
}
