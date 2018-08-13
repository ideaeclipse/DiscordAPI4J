package DiscordAPI.objects;

public class Payloads {
    public Payloads(){

    }
    public enum GameTypes {
        Playing,
        Streaming,
        Listening
    }

    public enum ChannelTypes {
        textChannel,
        dmChannel
    }

    class DMessage {
        Long channel_id;
        Long id;
        Long guild_id;
        String content;
        public DMessage(){

        }
    }

    class DGame {
        String name;
        String state;
        String details;
        GameTypes type;
        public DGame(){

        }
    }

    class DChannel {
        Long id;
        ChannelTypes type;
        String name;
        Integer position;
        Boolean nsfw;
        public DChannel(){

        }
    }

    class DUser {
        Long id;
        String username;
        Integer discriminator;
        public DUser(){

        }
    }

    class DRole {
        Long permissions;
        String name;
        Integer position;
        Integer color;
        Long id;
        public DRole(){

        }
    }

    public class DWelcome {
        public String _trace;
        public Long heartbeat_interval;
    }
}
