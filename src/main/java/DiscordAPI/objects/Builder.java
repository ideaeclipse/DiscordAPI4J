package DiscordAPI.objects;

import DiscordAPI.webSocket.OpCodes;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * This class is used for Building JSONObject {@link JSONObject}
 * Based on the payload structure Discord's Websocket handles
 *
 * @author Myles
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
        JSONObject properties;
        Boolean compress = true;
        Integer large_threshold = 250;
        //Presence Class
        JSONObject presence;
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
            JSONObject game;
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
    public static JSONObject buildPayload(OpCodes code, Object data) {
        JSONObject object = new JSONObject();
        object.put("op", code.ordinal());
        object.put("d", data);
        return object;
    }

    /**
     * This method is used for the Object param in buildPayload {@link Builder#buildPayload(OpCodes, Object)}
     *
     * @param generic is an instance of an Object that in the Builder Class {@link Builder}
     * @param <T> is the generic Type
     * @return Returns a Json Data used for the 'd' value in the Wss payload
     */
    static <T> JSONObject buildData(T generic) {
        JSONObject data = new JSONObject();
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
