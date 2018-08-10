package DiscordAPI.webSocket.jsonData.identity;

import DiscordAPI.webSocket.jsonData.IJSONObject;

public enum IDENTITY implements IJSONObject {
    token(String.class, null),
    //PROPERTIES.class
    properties(PROPERTIES.class, null),
    compress(Boolean.class, true),
    large_threshold(Integer.class, 250),
    //PRESENCE.class
    presence(PRESENCE.class, null);

    private Class<?> aClass;
    private Object o;

    IDENTITY(Class<?> stringClass, Object o) {
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
    public enum PRESENCE implements IJSONObject {
        //GAME.class
        game(GAME.class, null),
        status(String.class, "online"),
        since(long.class, null),
        afk(Boolean.class, false);

        private Class<?> aClass;
        private Object o;

        PRESENCE(Class<?> gameClass, Object o) {
            this.o = o;
            this.aClass = gameClass;
        }

        @Override
        public Class<?> getaClass() {
            return aClass;
        }

        @Override
        public Object getDefaultValue() {
            return o;
        }
    }
    public enum PROPERTIES implements IJSONObject {
        $os(String.class, System.getProperty("os.name")),
        $browser(String.class, "D4J"),
        $device(String.class, "D4J");

        private Class<?> aClass;
        private Object o;

        PROPERTIES(Class<?> stringClass, Object o) {
            this.aClass = stringClass;
            this.o = o;
        }
        @Override
        public Object getDefaultValue() {
            return this.o;
        }
        @Override
        public Class<?> getaClass() {
            return aClass;
        }
    }
    public enum GAME implements IJSONObject {
        name(String.class, "cm help for commands"),
        type(Integer.class, 0);

        private Class<?> aClass;
        private Object o;

        GAME(Class<?> integerClass, Object o) {
            this.aClass = integerClass;
            this.o = o;
        }

        @Override
        public Class<?> getaClass() {
            return aClass;
        }

        @Override
        public Object getDefaultValue() {
            return o;
        }
    }
}
