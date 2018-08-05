package DiscordAPI.WebSocket.Utils.Parsers;

public class Payloads {
    public class Channel {
        public Long id;
        public String name;
        public Integer position;
        public Boolean nsfw;
    }

    public class Message {
        public Long channel_id;
        public Long id;
        public Long guild_id;
        public String content;
    }

    public class User {
        public String username;
        public Integer discriminator;
        public Long id;
    }

    public class Role {
        public Long permissions;
        public String name;
        public Integer position;
        public Integer color;
        public Long id;
    }

    public class Welcome {
        public String _trace;
        public Long heartbeat_interval;
    }
}
