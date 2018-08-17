package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;
import DiscordAPI.utils.Json;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * This class is called from each listenerEvent {@link DiscordAPI.listener.dispatcher.listenerEvents.Channel_Create}
 * Each class logs there event using {@link DiscordLogger}
 *
 * @author Ideaeclipse
 */
public class Parser {
    /**
     * Parses payload sent from {@link DiscordAPI.webSocket.Wss} and converts it to a channel
     *
     * @author Ideaeclipse
     * @see DiscordAPI.webSocket.WebSocket_Events#CHANNEL_CREATE
     */
    public static class ChannelCreate {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Channel channel;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link DiscordAPI.webSocket.Wss}
         */
        public ChannelCreate(final IDiscordBot b, final Json payload) {
            Channel.ChannelP cd = new Channel.ChannelP(payload).logic();
            channel = cd.getChannel();
            if (channel.getType().equals(Payloads.ChannelTypes.textChannel)) {
                b.updateChannels();
                logger.info("Text Channel Create: Channel Name: " + channel.getName() + " NSFW: " + channel.getNsfw() + " Position: " + channel.getPosition());
            } else if (channel.getType().equals(Payloads.ChannelTypes.dmChannel)) {
                logger.info("Dm Channel Created");
            }
        }

        public Channel getChannel() {
            return channel;
        }
    }

    /**
     * Parses payload sent from {@link DiscordAPI.webSocket.Wss} and converts it to a channel
     *
     * @author Ideaeclipse
     * @see DiscordAPI.webSocket.WebSocket_Events#CHANNEL_DELETE
     */
    public static class ChannelDelete {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Channel channel;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link DiscordAPI.webSocket.Wss}
         */
        public ChannelDelete(final IDiscordBot b, final Json payload) {
            Channel.ChannelP cd = new Channel.ChannelP(payload).logic();
            channel = cd.getChannel();
            b.updateChannels();
            logger.info("Channel Delete: Channel Name: " + channel.getName() + " NSFW: " + channel.getNsfw() + " Position: " + channel.getPosition());
        }

        public Channel getChannel() {
            return channel;
        }
    }

    /**
     * Parses payload sent from {@link DiscordAPI.webSocket.Wss} and converts it to a old channel and new channel
     *
     * @author Ideaeclipse
     * @see DiscordAPI.webSocket.WebSocket_Events#CHANNEL_UPDATE
     */
    public static class ChannelUpdate {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Channel oldC;
        private volatile Channel newC;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link DiscordAPI.webSocket.Wss}
         */
        public ChannelUpdate(final IDiscordBot b, final Json payload) {
            Channel.ChannelP cd = new Channel.ChannelP(payload).logic();
            oldC = DiscordUtils.Search.CHANNEL(b.getChannels(), cd.getChannel().getName());
            newC = cd.getChannel();
            b.updateChannels();
            logger.info("Channel Update Old: Name: " + oldC.getName() + " NSFW: " + oldC.getNsfw() + " Position: " + oldC.getPosition());
            logger.info("Channel Update New: Name: " + newC.getName() + " NSFW: " + newC.getNsfw() + " Position: " + newC.getPosition());
        }

        public Channel getOldChannel() {
            return oldC;
        }

        public Channel getNewChannel() {
            return newC;
        }
    }

    /**
     * Parses payload sent from {@link DiscordAPI.webSocket.Wss} and converts it to a message
     *
     * @author Ideaeclipse
     * @see DiscordAPI.webSocket.WebSocket_Events#MESSAGE_CREATE
     * @see DiscordAPI.objects.Payloads.ChannelTypes
     */
    public static class MessageCreate {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Message message;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link DiscordAPI.webSocket.Wss}
         */
        public MessageCreate(final IDiscordBot b, final Json payload) {
            Json user = new Json(String.valueOf(payload.get("author")));
            Payloads.DUser u = convertToPayload(user, Payloads.DUser.class);
            Payloads.DMessage m = convertToPayload(payload, Payloads.DMessage.class);
            User.UserP pd = new User.UserP(u.id, b).logic();
            Channel.ChannelP cd = new Channel.ChannelP(m.channel_id).logic();
            System.out.println(cd.getChannel());
            System.out.println(m.guild_id + " " + m.content);
            message = new Message(pd.getUser(), cd.getChannel(), m.guild_id, m.content);
            if (message.getChannel().getType().equals(Payloads.ChannelTypes.textChannel)) {
                logger.info("Message Create: User: " + message.getUser().getName() + " Content: " + message.getContent().replace("\n", "\\n") + " Channel: " + message.getChannel().getName());
            } else {
                logger.info("Dm Sent: User: " + message.getUser().getName() + " Content: " + message.getContent().replace("\n", "\\n"));
            }
        }


        public Message getMessage() {
            return this.message;
        }
    }

    /**
     * Parses payload sent from {@link DiscordAPI.webSocket.Wss} and converts it to a Status
     *
     * @author Ideaeclipse
     * @see DiscordAPI.webSocket.WebSocket_Events#PRESENCE_UPDATE
     */
    public static class PresenceUpdate {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Status status;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link DiscordAPI.webSocket.Wss}
         */
        public PresenceUpdate(final IDiscordBot b, final Json payload) {
            final Payloads.DUser user = convertToPayload(new Json(String.valueOf(payload.get("user"))), Payloads.DUser.class);
            final User.UserP pd = new User.UserP(user.id, b).logic();
            final Game.GameP gd = payload.get("game") != null ? new Game.GameP(new Json((String) payload.get("game"))).logic() : null;
            status = new Status(gd != null ? gd.getGame() : null, pd.getUser(), String.valueOf(payload.get("status")));
            logger.info("Presence Update: User: " + status.getUser().getName() + " Status: " + status.getStatus() + (status.getGame() != null ? " Game: " + ((status.getGame().getType() == Payloads.GameTypes.Playing) ?
                    "Playing " + status.getGame().getName() + " Details: " + status.getGame().getState() + " " + status.getGame().getDetails()
                    : "Listening to " + status.getGame().getState() + " Song: " + status.getGame().getDetails() + " on " + status.getGame().getName()) : ""));
        }

        public Status getStatus() {
            return status;
        }
    }

    /**
     * This method takes in a Json and a Payload type {@link Payloads}
     *
     * @param <T>    {@link Payloads}
     * @param object Payload from {@link DiscordAPI.webSocket.Wss}
     * @param c      {@link Payloads#*#getClass()}
     * @return {@link Payloads#*}
     */
    public static <T> T convertToPayload(final Json object, final Class<?> c) {
        T o = getObject(c);
        try {
            for (Object s : object.keySet()) {
                Field f;
                try {
                    f = c.getDeclaredField(String.valueOf(s));
                    f.setAccessible(true);
                } catch (NoSuchFieldException ignored) {
                    continue;
                }
                String value = String.valueOf(object.get((String) s));
                if (f.getType().equals(String.class)) {
                    f.set(o, value);
                } else if (f.getType().equals(Integer.class)) {
                    f.set(o, Integer.parseInt(value));
                } else if (f.getType().equals(Float.class)) {
                    f.set(o, Float.parseFloat(value));
                } else if (f.getType().equals(Long.class)) {
                    f.set(o, Long.parseUnsignedLong(value));
                } else if (f.getType().equals(Boolean.class)) {
                    f.set(o, Boolean.parseBoolean(value));
                } else if (f.getType().isEnum()) {
                    f.set(o, f.getType().getEnumConstants()[Integer.parseInt(value)]);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    /**
     * Creates a new Instance of the class passed
     *
     * @param c   {@link Payloads#*}
     * @param <T> {@link Payloads}
     * @return new instance of <T>
     */
    private static <T> T getObject(final Class<?> c) {
        T o = null;
        try {
            o = (T) c.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return o;
    }
}
