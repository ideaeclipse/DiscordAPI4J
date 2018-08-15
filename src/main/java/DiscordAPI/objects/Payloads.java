package DiscordAPI.objects;

import org.json.simple.JSONObject;

/**
 * This class is used for {@link Parser#convertToPayload(JSONObject, Class)}
 * and will return the class you passed to the method
 *
 * @author Ideaeclipse
 */
public class Payloads {
    /**
     * GameTypes use {@link GameTypes#ordinal()} to get integer value
     *
     * @see Game
     */
    public enum GameTypes {
        Playing,
        Streaming,
        Listening
    }

    /**
     * ChannelTypes use {@link ChannelTypes#ordinal()} to get integer value
     *
     * @see Channel
     */
    public enum ChannelTypes {
        textChannel,
        dmChannel
    }

    /**
     * This class stores Message data
     *
     * @author Ideaeclipse
     * @see Message
     * @see DiscordAPI.objects.Parser.MessageCreate
     * @see DiscordAPI.listener.dispatcher.listenerEvents.Message_Create
     */
    static class DMessage {
        Long channel_id;
        Long id;
        Long guild_id;
        String content;

        public DMessage() {

        }
    }

    /**
     * This class stores Game data
     *
     * @author Ideaeclipse
     * @see Game
     * @see DiscordAPI.objects.Builder.Identity
     * @see DiscordBot#setStatus(GameTypes, String)
     * @see GameTypes
     */
    static class DGame {
        String name;
        String state;
        String details;
        GameTypes type;

        public DGame() {

        }
    }

    /**
     * This class stores Channel data
     *
     * @author Ideaeclipse
     * @see Channel
     * @see DiscordAPI.objects.Parser.ChannelCreate
     * @see DiscordAPI.objects.Parser.ChannelDelete
     * @see DiscordAPI.objects.Parser.ChannelUpdate
     * @see DiscordAPI.listener.dispatcher.listenerEvents.Channel_Create
     * @see DiscordAPI.listener.dispatcher.listenerEvents.Channel_Delete
     * @see DiscordAPI.listener.dispatcher.listenerEvents.Channel_Update
     */
    static class DChannel {
        Long id;
        ChannelTypes type;
        String name;
        Integer position;
        Boolean nsfw;

        public DChannel() {

        }
    }

    /**
     * This class stores User data
     *
     * @author Ideaeclipse
     * @see User
     */
    static class DUser {
        Long id;
        String username;
        Integer discriminator;

        public DUser() {

        }
    }

    /**
     * This class stores Role data
     *
     * @author Ideaeclipse
     * @see Role
     */
    static class DRole {
        Long permissions;
        String name;
        Integer position;
        Integer color;
        Long id;

        public DRole() {

        }
    }

    /**
     * This class stores Welcome data this is the first message {@link DiscordAPI.webSocket.Wss} receives apon connecting
     *
     * @author Ideaeclipse
     */
    public static class DWelcome {
        public String _trace;
        public Long heartbeat_interval;
    }
}
