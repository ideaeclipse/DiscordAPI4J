package DiscordAPI.listener.discordApiListener.listenerEvents;

import DiscordAPI.IPrivateBot;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.Json;
import ideaeclipse.reflectionListener.Event;

public class Voice_Server_Update extends Event {
    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public Voice_Server_Update(final IPrivateBot b, final Json payload) {
        Parser.VoiceServerUpdate voiceServerUpdate = new Parser.VoiceServerUpdate(b, payload);
    }
}
