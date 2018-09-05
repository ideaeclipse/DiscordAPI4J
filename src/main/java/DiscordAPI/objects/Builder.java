package DiscordAPI.objects;

import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.TextOpCodes;
import DiscordAPI.webSocket.VoiceOpCodes;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class is used for Building Json {@link JSONObject}
 * Based on the payload structure Discord's Websocket handles
 * SurpressWarnings due to not used warnings because data is accessed
 * using java reflections
 *
 * @author Ideaeclipse
 */
@SuppressWarnings("ALL")
public class Builder {
    static class CreateDmChannel {
        Long recipient_id;
    }

    static class VoiceStateUpdate {
        Long guild_id;
        Long channel_id;
        Boolean self_mute = false;
        Boolean self_deaf = false;
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
    static class Identity {
        String token;
        //Properties Class
        Map properties;
        Boolean compress = true;
        Integer large_threshold = 250;
        //Presence Class
        Map presence;
        ArrayList<Integer> shards = new Shard().getInts();

        /**
         * Used for creating an Shard object used in the identity Payload
         * This is used in {@link DiscordBot#buildIdentity()}
         */
        static class Shard {
            ArrayList<Integer> ints = new ArrayList<>();

            Shard() {
                ints.add(0);
                ints.add(1);
            }

            ArrayList<Integer> getInts() {
                return ints;
            }
        }

        /**
         * Used for creating an Presnece object used in the identity Payload
         * This is used in {@link DiscordBot#buildIdentity()}
         */
        static class Presence {
            //Game
            Map game;
            String status = "online";
            Long since = null;
            Boolean afk = false;

            /**
             * Used for creating an Game object used in the identity Payload
             * This is used in {@link DiscordBot#buildIdentity()}
             */
            static class Game {
                String name = "cm help for commands";
                Integer type = Payloads.GameTypes.Playing.ordinal();
            }
        }

        /**
         * Used for creating an Properties object used in the identity Payload
         * This is used in {@link DiscordBot#buildIdentity()}
         */
        static class Properties {
            String $os = System.getProperty("os.name");
            String $browser = "D4J";
            String $device = "D4J";
        }
    }

    /**
     * Method is used to create a payload to send over the Discord TextWss {@link DiscordAPI.webSocket.Wss}
     *
     * @param code OpCode Value {@link TextOpCodes}
     * @param data Json Object to file 'd' param use {@link Builder#buildData(Object)}
     * @return Complete payload ready to send over the websocket
     */
    public static Json buildPayload(TextOpCodes code, Object data) {
        Json object = new Json();
        object.put("op", code.ordinal());
        object.put("d", data);
        return object;
    }

    public static Json buildPayload(VoiceOpCodes code, Object data) {
        Json object = new Json();
        object.put("op", code.ordinal());
        object.put("d", data);
        return object;
    }

    /**
     * This method is used for the Object param in buildPayload {@link Builder#buildPayload(TextOpCodes, Object)}
     *
     * @param <T>     is the generic Type
     * @param generic is an instance of an Object that in the Builder Class {@link Builder}
     * @return Returns a Json DiscordBot.Data used for the 'd' value in the Wss payload
     */
    public static <T> Map<String, Object> buildData(T generic) {
        Json data = new Json();
        for (Field f : generic.getClass().getDeclaredFields()) {
            try {
                data.put(f.getName(), f.get(generic));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data.getMap();
    }
}
