package DiscordAPI.objects;

import DiscordAPI.webSocket.OpCodes;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Builder {
    static class CreateDmChannel {
        Long recipient_id;
    }

    static class Identity {
        String token;
        //Properties Class
        JSONObject properties;
        Boolean compress = true;
        Integer large_threshold = 250;
        //Presence Class
        JSONObject presence;
        ArrayList<Integer> shards = new Shard().getInts();

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

        static class Presence {
            //Game
            JSONObject game;
            String status = "online";
            Long since = null;
            Boolean afk = false;

            static class Game {
                String name = "cm help for commands";
                Integer type = Payloads.GameTypes.Playing.ordinal();
            }
        }

        static class Properties {
            String $os = System.getProperty("os.name");
            String $browser = "D4J";
            String $device = "D4J";
        }
    }

    public static JSONObject buildPayload(OpCodes code, Object data) {
        JSONObject object = new JSONObject();
        object.put("op", code.ordinal());
        object.put("d", data);
        return object;
    }

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
