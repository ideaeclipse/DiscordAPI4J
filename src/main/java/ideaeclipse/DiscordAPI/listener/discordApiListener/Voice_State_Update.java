package ideaeclipse.DiscordAPI.listener.discordApiListener;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.ParserObjects;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class Voice_State_Update extends Event {
    /**
     * @param b DiscordBot {@link ideaeclipse.DiscordAPI.objects.DiscordBot}
     */
    public Voice_State_Update(final IPrivateBot b, final Json payload) {
        ParserObjects.VoiceStateUpdate voiceStateUpdate = new ParserObjects.VoiceStateUpdate(b, payload);
    }
}
