package DiscordAPI.objects;

import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.OpCodes;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * This class is used for Building Json {@link JSONObject}
 * Based on the payload structure Discord's Websocket handles
 *
 * @author Ideaeclipse
 */
public class Builder {
    static class CreateDmChannel {
        Long recipient_id;
    }

    /**
     * Used for creating an identity payload
     * This is used in {@link DiscordBot#buildIdentity()}
     */
    static class Identity {
        String token;
        //Properties Class
        Json properties;
        Boolean compress = true;
        Integer large_threshold = 250;
        //Presence Class
        Json presence;
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
            Json game;
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
     * @param code OpCode Value {@link OpCodes}
     * @param data Json Object to file 'd' param use {@link Builder#buildData(Object)}
     * @return Complete payload ready to send over the websocket
     */
    public static Json buildPayload(OpCodes code, Object data) {
        Json object = new Json();
        object.put("op", code.ordinal());
        object.put("d", data);
        return object;
    }

    /**
     * This method is used for the Object param in buildPayload {@link Builder#buildPayload(OpCodes, Object)}
     *
     * @param <T> is the generic Type
     * @param generic is an instance of an Object that in the Builder Class {@link Builder}
     * @return Returns a Json Data used for the 'd' value in the Wss payload
     */
    static <T> Json buildData(T generic) {
        Json data = new Json();
        for (Field f : generic.getClass().getDeclaredFields()) {
            try {
                data.put(f.getName(), f.get(generic));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
