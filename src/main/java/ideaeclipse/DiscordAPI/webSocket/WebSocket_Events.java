package ideaeclipse.DiscordAPI.webSocket;

import ideaeclipse.DiscordAPI.listener.discordApiListener.*;

public enum WebSocket_Events {
    PRESENCE_UPDATE(Presence_Update.class),
    MESSAGE_CREATE(Message_Create.class),
    CHANNEL_CREATE(Channel_Create.class),
    CHANNEL_DELETE(Channel_Delete.class),
    CHANNEL_UPDATE(Channel_Update.class);
    //VOICE_SERVER_UPDATE(Voice_Server_Update.class),
    //VOICE_STATE_UPDATE(Voice_State_Update.class);
    private Class<?> aClass;

    WebSocket_Events(Class<?> clas) {
        this.aClass = clas;
    }

    public Class<?> getaClass() {
        return aClass;
    }
}
