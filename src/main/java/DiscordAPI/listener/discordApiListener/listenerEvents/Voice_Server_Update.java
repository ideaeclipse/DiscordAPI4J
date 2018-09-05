package DiscordAPI.listener.discordApiListener.listenerEvents;

import DiscordAPI.IPrivateBot;
import DiscordAPI.listener.discordApiListener.ApiListener;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.Json;

public class Voice_Server_Update extends ApiListener {
    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public Voice_Server_Update(final IPrivateBot b, final Json payload) {
        super(b);
        Parser.VoiceServerUpdate voiceServerUpdate = new Parser.VoiceServerUpdate(b, payload);
    }
}
