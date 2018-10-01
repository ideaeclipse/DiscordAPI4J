package DiscordAPI.objects;

import DiscordAPI.listener.discordApiListener.listenerEvents.Channel_Create;
import DiscordAPI.listener.discordApiListener.listenerEvents.Channel_Delete;
import DiscordAPI.listener.discordApiListener.listenerEvents.Channel_Update;
import DiscordAPI.listener.discordApiListener.listenerEvents.Message_Create;
import ideaeclipse.JsonUtilities.Json;

import java.util.List;

/**
 * This class is used for {@link ParserObjects.convertToPayload(Json, Class)}
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
     * @see DiscordAPI.objects.ParserObjects.MessageCreate
     * @see Message_Create
     */
    public static class DMessage {
        public Long channel_id;
        public Long id;
        public Long guild_id;
        public String content;

        public DMessage() {

        }
    }

    public static class DVoiceServerUpdate {
        public String token;
        public Long guild_id;
        public String endpoint;

        public DVoiceServerUpdate() {

        }
    }

    public static class DVoiceStateUpdate {
        public Long user_id;
        public Boolean suppress;
        public String session_id;
        public Boolean self_video;
        public Boolean self_mute;
        public Boolean mute;
        public Boolean deaf;
        public Long guild_id;
        public Long channel_id;

        public DVoiceStateUpdate() {

        }
    }

    /**
     * This class stores Game data
     *
     * @author Ideaeclipse
     * @see Game
     * @see DiscordAPI.objects.BuilderObjects.Identity
     * @see DiscordBot#setStatus(GameTypes, String)
     * @see GameTypes
     */
    public static class DGame {
        public String name;
        public String state;
        public String details;
        public GameTypes type;

        public DGame() {

        }
    }

    /**
     * This class stores Channel data
     *
     * @author Ideaeclipse
     * @see Channel
     * @see DiscordAPI.objects.ParserObjects.ChannelCreate
     * @see DiscordAPI.objects.ParserObjects.ChannelDelete
     * @see DiscordAPI.objects.ParserObjects.ChannelUpdate
     * @see Channel_Create
     * @see Channel_Delete
     * @see Channel_Update
     */
    public static class DChannel {
        public Long id;
        public ChannelTypes type;
        public String name;
        public Integer position;
        public Boolean nsfw;

        public DChannel() {

        }
    }

    /**
     * This class stores DiscordUser data
     *
     * @author Ideaeclipse
     * @see DiscordUser
     */
    public static class DUser {
        public Long id;
        public String username;
        public Integer discriminator;
        public Boolean deaf;
        public Boolean mute;

        public DUser() {

        }
    }

    public static class DServerUniqueUser {
        public String nick;
        public String joined_at;
        public List<Long> roles;
        public Boolean deaf;
        public Boolean mute;
        public String session_id;
        public DiscordUser user;
        public String status;
        public Game game;

        public DServerUniqueUser() {

        }
    }

    /**
     * This class stores Role data
     *
     * @author Ideaeclipse
     * @see Role
     */
    public static class DRole {
        public Long permissions;
        public String name;
        public Integer position;
        public Integer color;
        public Long id;

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
