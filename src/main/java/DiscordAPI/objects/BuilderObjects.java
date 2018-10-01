package DiscordAPI.objects;

import java.util.ArrayList;
import java.util.Map;

public class BuilderObjects {
    public static class CreateDmChannel {
        public Long recipient_id;
    }

    public static class VoiceStateUpdate {
        public Long guild_id;
        public Long channel_id;
        public Boolean self_mute = false;
        public Boolean self_deaf = false;
    }

    public static class VoiceIdentify {
        public Long server_id;
        public Long user_id;
        public String session_id;
        public String token;
    }

    /**
     * Used for creating an identity payload
     * This is used in {@link DiscordBot#buildIdentity()}
     */
    public static class Identity {
        public String token;
        //Properties Class
        public Map properties;
        public Boolean compress = true;
        public Integer large_threshold = 250;
        //Presence Class
        public Map presence;
        public ArrayList<Integer> shards = new Shard().getInts();

        /**
         * Used for creating an Shard object used in the identity Payload
         * This is used in {@link DiscordBot#buildIdentity()}
         */
        public static class Shard {
            public ArrayList<Integer> ints = new ArrayList<>();

            public Shard() {
                ints.add(0);
                ints.add(1);
            }

            public ArrayList<Integer> getInts() {
                return ints;
            }
        }

        /**
         * Used for creating an Presnece object used in the identity Payload
         * This is used in {@link DiscordBot#buildIdentity()}
         */
        public static class Presence {
            //Game
            public Map game;
            public String status = "online";
            public Long since = null;
            public Boolean afk = false;

            /**
             * Used for creating an Game object used in the identity Payload
             * This is used in {@link DiscordBot#buildIdentity()}
             */
            public static class Game {
                public String name = "cm help for commands";
                public Integer type = Payloads.GameTypes.Playing.ordinal();
            }
        }

        /**
         * Used for creating an Properties object used in the identity Payload
         * This is used in {@link DiscordBot#buildIdentity()}
         */
        public static class Properties {
            public String $os = System.getProperty("os.name");
            public String $browser = "D4J";
            public String $device = "D4J";
        }
    }
}
