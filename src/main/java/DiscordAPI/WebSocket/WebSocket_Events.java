package DiscordAPI.WebSocket;

import DiscordAPI.listener.Dispatcher.ListenerEvents.Message_Create;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Presence_Update;

public enum WebSocket_Events {
    PRESENCE_UPDATE(Presence_Update.class),
    MESSAGE_CREATE(Message_Create.class);
    private Class<?> aClass;

    WebSocket_Events(Class<?> clas) {
        this.aClass = clas;
    }

    public Class<?> getaClass() {
        return aClass;
    }
}
