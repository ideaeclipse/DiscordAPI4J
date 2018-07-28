package DiscordAPI.WebSocket.Voice.VoiceStateUpdate;

import DiscordAPI.WebSocket.JsonData.IJSONObject;

public enum VSU implements IJSONObject {
    guild_id(Long.class,null),
    self_mute(Boolean.class,false),
    self_deaf(Boolean.class,false);

    private Class<?> aClass;
    private Object o;

    VSU(Class<?> stringClass, Object o) {
        this.aClass = stringClass;
        this.o = o;
    }

    @Override
    public Class<?> getaClass() {
        return this.aClass;
    }

    @Override
    public Object getDefaultValue() {
        return this.o;
    }
}
