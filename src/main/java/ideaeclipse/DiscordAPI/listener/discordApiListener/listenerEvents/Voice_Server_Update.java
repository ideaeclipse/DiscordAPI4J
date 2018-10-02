package ideaeclipse.DiscordAPI.listener.discordApiListener.listenerEvents;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.ParserObjects;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.Parser;
import ideaeclipse.reflectionListener.Event;

public class Voice_Server_Update extends Event {
    /**
     * @param b DiscordBot {@link ideaeclipse.DiscordAPI.objects.DiscordBot}
     */
    public Voice_Server_Update(final IPrivateBot b, final Json payload) {
        ParserObjects.VoiceServerUpdate voiceServerUpdate = new  ParserObjects.VoiceServerUpdate(b, payload);
    }
}
