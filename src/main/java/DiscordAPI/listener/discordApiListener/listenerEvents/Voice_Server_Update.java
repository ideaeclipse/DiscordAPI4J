package DiscordAPI.listener.discordApiListener.listenerEvents;

import DiscordAPI.IPrivateBot;
import DiscordAPI.objects.ParserObjects;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.Parser;
import ideaeclipse.reflectionListener.Event;

public class Voice_Server_Update extends Event {
    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public Voice_Server_Update(final IPrivateBot b, final Json payload) {
        ParserObjects.VoiceServerUpdate voiceServerUpdate = new  ParserObjects.VoiceServerUpdate(b, payload);
    }
}
