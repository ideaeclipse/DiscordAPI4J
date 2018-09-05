package DiscordAPI.objects;

import DiscordAPI.listener.discordApiListener.listenerEvents.Channel_Create;
import DiscordAPI.listener.discordApiListener.listenerEvents.Channel_Delete;
import DiscordAPI.listener.discordApiListener.listenerEvents.Channel_Update;
import DiscordAPI.listener.discordApiListener.listenerEvents.Message_Create;
import DiscordAPI.utils.Json;

import java.util.List;

/**
 * This class is used for {@link Parser#convertToPayload(Json, Class)}
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
        dmChannel,
        voiceChannel
    }

    /**
     * This class stores Message data
     *
     * @author Ideaeclipse
     * @see Message
     * @see DiscordAPI.objects.Parser.MessageCreate
     * @see Message_Create
     */
    static class DMessage {
        Long channel_id;
        Long id;
        Long guild_id;
        String content;

        public DMessage() {

        }
    }

    static class DVoiceServerUpdate {
        String token;
        Long guild_id;
        String endpoint;

        public DVoiceServerUpdate() {

        }
    }

    static class DVoiceStateUpdate {
        Long user_id;
        Boolean suppress;
        String session_id;
        Boolean self_video;
        Boolean self_mute;
        Boolean mute;
        Boolean deaf;
        Long guild_id;
        Long channel_id;

        public DVoiceStateUpdate() {

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
     * @see Channel_Create
     * @see Channel_Delete
     * @see Channel_Update
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
     * This class stores DiscordUser data
     *
     * @author Ideaeclipse
     * @see DiscordUser
     */
    static class DUser {
        Long id;
        String username;
        Integer discriminator;
        Boolean deaf;
        Boolean mute;

        public DUser() {

        }
    }

    static class DServerUniqueUser {
        String nick;
        String joined_at;
        List<Long> roles;
        Boolean deaf;
        Boolean mute;
        String session_id;
        DiscordUser user;
        String status;
        Game game;

        public DServerUniqueUser() {

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

    public static class General {
        public Integer op;
        public Json d;
        public String t;
        public Integer s;
    }
}
