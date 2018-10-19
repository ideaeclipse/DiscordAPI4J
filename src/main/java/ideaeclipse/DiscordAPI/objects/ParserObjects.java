package ideaeclipse.DiscordAPI.objects;

import ideaeclipse.AsyncUtility.AsyncList;
import ideaeclipse.AsyncUtility.ForEachList;
import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.listener.discordApiListener.Channel_Create;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.customLogger.CustomLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This class is called from each listenerEvent {@link Channel_Create}
 * Each class logs there event using CustomLogger
 *
 * @author Ideaeclipse
 */
public class ParserObjects {
    /**
     * Parses payload sent from {@link ideaeclipse.DiscordAPI.webSocket.Wss} and converts it to a channel
     *
     * @author Ideaeclipse
     * @see ideaeclipse.DiscordAPI.webSocket.WebSocket_Events#CHANNEL_CREATE
     */
    public static class ChannelCreate {
        private final CustomLogger logger = new CustomLogger(this.getClass());
        private volatile IChannel channel;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
         */
        public ChannelCreate(final IPrivateBot b, final Json payload) {
            Channel.ChannelP cd = new Channel.ChannelP(payload).logic();
            channel = cd.getChannel();
            if (channel.getType().equals(Payloads.ChannelTypes.textChannel)) {
                b.updateChannels();
                logger.info("Text Channel Create: Channel Name: " + channel.getName() + " NSFW: " + channel.getNsfw() + " Position: " + channel.getPosition());
            } else if (channel.getType().equals(Payloads.ChannelTypes.dmChannel)) {
                logger.info("Dm Channel Created");
            }
        }

        public IChannel getChannel() {
            return channel;
        }
    }

    /**
     * Parses payload sent from {@link ideaeclipse.DiscordAPI.webSocket.Wss} and converts it to a channel
     *
     * @author Ideaeclipse
     * @see ideaeclipse.DiscordAPI.webSocket.WebSocket_Events#CHANNEL_DELETE
     */
    public static class ChannelDelete {
        private final CustomLogger logger = new CustomLogger(this.getClass());
        private volatile IChannel channel;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
         */
        public ChannelDelete(final IPrivateBot b, final Json payload) {
            Channel.ChannelP cd = new Channel.ChannelP(payload).logic();
            channel = cd.getChannel();
            //b.updateChannels();
            logger.info("Channel Delete: Channel Name: " + channel.getName() + " NSFW: " + channel.getNsfw() + " Position: " + channel.getPosition());
        }

        public IChannel getChannel() {
            return channel;
        }
    }

    /**
     * Parses payload sent from {@link ideaeclipse.DiscordAPI.webSocket.Wss} and converts it to a old channel and new channel
     *
     * @author Ideaeclipse
     * @see ideaeclipse.DiscordAPI.webSocket.WebSocket_Events#CHANNEL_UPDATE
     */
    public static class ChannelUpdate {
        private final CustomLogger logger = new CustomLogger(this.getClass());
        private volatile IChannel oldC;
        private volatile IChannel newC;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
         */
        public ChannelUpdate(final IPrivateBot b, final Json payload) {
            Channel.ChannelP cd = new Channel.ChannelP(payload).logic();
            oldC = DiscordUtils.Search.CHANNEL(b.getChannels(), cd.getChannel().getName());
            newC = cd.getChannel();
            // b.updateChannels();
            logger.info("Channel Update Old: Name: " + oldC.getName() + " NSFW: " + oldC.getNsfw() + " Position: " + oldC.getPosition());
            logger.info("Channel Update New: Name: " + newC.getName() + " NSFW: " + newC.getNsfw() + " Position: " + newC.getPosition());
        }

        public IChannel getOldChannel() {
            return oldC;
        }

        public IChannel getNewChannel() {
            return newC;
        }
    }

    /**
     * Parses payload sent from {@link ideaeclipse.DiscordAPI.webSocket.Wss} and converts it to a message
     *
     * @author Ideaeclipse
     * @see ideaeclipse.DiscordAPI.webSocket.WebSocket_Events#MESSAGE_CREATE
     * @see ideaeclipse.DiscordAPI.objects.Payloads.ChannelTypes
     */
    public static class MessageCreate {
        private final CustomLogger logger = new CustomLogger(this.getClass());
        private volatile Message message;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
         */
        public MessageCreate(final IPrivateBot b, final Json payload) {
            Json user = new Json(String.valueOf(payload.get("author")));
            Payloads.DMessage m = ParserObjects.convertToPayload(payload, Payloads.DMessage.class);
            DiscordUser.UserP pd = new DiscordUser.UserP(user, b).logic();
            Channel.ChannelP cd = new Channel.ChannelP(m.channel_id).logic();
            message = new Message(pd.getUser(), cd.getChannel(), m.guild_id, (m.content == null) ? "N/A" : m.content);
            if (message.getChannel().getType().equals(Payloads.ChannelTypes.textChannel)) {
                logger.info("Message Create: DiscordUser: " + message.getUser().getName() + " Content: " + message.getContent().replace("\n", "\\n") + " Channel: " + message.getChannel().getName());
            } else {
                logger.info("Dm Sent: DiscordUser: " + message.getUser().getName() + " Content: " + message.getContent().replace("\n", "\\n"));
            }
        }


        public Message getMessage() {
            return this.message;
        }
    }

    /**
     * Parses payload sent from {@link ideaeclipse.DiscordAPI.webSocket.Wss} and converts it to a Status
     *
     * @author Ideaeclipse
     * @see ideaeclipse.DiscordAPI.webSocket.WebSocket_Events#PRESENCE_UPDATE
     */
    public static class PresenceUpdate {
        private final CustomLogger logger = new CustomLogger(this.getClass());
        private final User user;

        /**
         * @param b       DiscordBot
         * @param payload Payload from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
         */
        public PresenceUpdate(final IPrivateBot b, final Json payload) {
            final User.ServerUniqueUserP u = new User.ServerUniqueUserP(b, payload).logic();
            user = u.getServerUniqueUser();
            logger.info("Presence Update: DiscordUser: " + user.getDiscordUser().getName() + " Status: " + user.getStatus() + (user.getGame() != null ? " Game: " + ((user.getGame().getType() == Payloads.GameTypes.Playing) ?
                    "Playing " + user.getGame().getName() + " Details: " + user.getGame().getState() + " " + user.getGame().getDetails()
                    : "Listening to " + user.getGame().getState() + " Song: " + user.getGame().getDetails() + " on " + user.getGame().getName()) : ""));
        }

        public User getUser() {
            return user;
        }
    }

    public static class VoiceServerUpdate {
        private final CustomLogger logger = new CustomLogger(this.getClass());
        private final VServerUpdate voiceServerUpdate;

        public VoiceServerUpdate(final IPrivateBot b, final Json payload) {
            Payloads.DVoiceServerUpdate vsu = ParserObjects.convertToPayload(payload, Payloads.DVoiceServerUpdate.class);
            this.voiceServerUpdate = new VServerUpdate(vsu.token, vsu.endpoint);
            synchronized (b.getAudioManager().getLock()) {
                b.getAudioManager().setVoiceServerUpdate(voiceServerUpdate);
                b.getAudioManager().getLock().notify();
            }
        }

        public VServerUpdate getVoiceServerUpdate() {
            return voiceServerUpdate;
        }
    }

    public static class VoiceStateUpdate {
        private final User user;
        private final VStateUpdate vStateUpdate;
        private final IChannel channel;

        public VoiceStateUpdate(final IPrivateBot b, final Json payload) {
            VStateUpdate.VStateUpdateP v = new VStateUpdate.VStateUpdateP(b, payload).logic();
            user = v.getUser();
            vStateUpdate = v.getvStateUpdate();
            channel = v.getChannel();
            if (b.getAudioManager().getInitialUpdate() == null)
                b.getAudioManager().setInitialUpdate(vStateUpdate);
        }
    }

    /**
     * This method takes in a Json and a Payload type {@link Payloads}
     *
     * @param <T>    {@link Payloads}
     * @param object Payload from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
     * @param c      {@link Payloads#*#getClass()}
     * @return {@link Payloads#*}
     */
    public static <T> T convertToPayload(final Json object, final Class<?> c) {
        T o = getObject(c);
        AsyncList<T> list = new ForEachList<>();
        for (Object s : object.keySet()) {
            list.add(x -> {
                try {
                    Field f = null;
                    try {
                        f = c.getDeclaredField(String.valueOf(s));
                        f.setAccessible(true);
                    } catch (NoSuchFieldException ignored) {
                        Thread.currentThread().interrupt();
                    }
                    String value = String.valueOf(object.get((String) s));
                    if (value.equals("null")) {
                        value = null;
                    }
                    if (f != null) {
                        if (f.getType().equals(List.class)) {
                            ParameterizedType genericType = (ParameterizedType) f.getGenericType();
                            f.set(o, convertToList(value, (Class<?>) genericType.getActualTypeArguments()[0]));
                        } else if (f.getType().equals(DiscordUser.class)) {
                            f.set(o, value != null ? new DiscordUser.UserP(new Json(value), DiscordUtils.DefaultLinks.bot).logic().getUser() : null);
                        } else if (f.getType().equals(Game.class)) {
                            f.set(o, value != null ? new Game.GameP(new Json(value)).logic().getGame() : null);
                        } else if (f.getType().equals(Json.class)) {
                            f.set(o, value != null ? new Json(value) : null);
                        } else if (f.getType().isEnum()) {
                            f.set(o, value != null ? f.getType().getEnumConstants()[Integer.parseInt(value)] : null);
                        } else {
                            f.set(o, value != null ? convert(value, f.getType()) : null);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return Optional.empty();
            });
        }
        list.execute();
        return o;
    }

    private static <T> List<T> convertToList(String value, Class<T> c) {
        List<T> list = new ArrayList<>();
        value = value.substring(1, value.length() - 1);
        List<Integer> indexes = new ArrayList<>();
        if (value.contains(",")) {
            int index = value.indexOf(",");
            while (index >= 0) {
                indexes.add(index);
                index = value.indexOf(",", (index + 1));
            }
            if (indexes.size() > 1) {
                for (int i = 0; i < indexes.size(); i++) {
                    if (i == 0) {
                        list.add(convert(value.substring(0, indexes.get(i)).trim(), c));
                    } else if (i == indexes.size() - 1) {
                        list.add(convert(value.substring(indexes.get(i - 1), indexes.get(i)).trim(), c));
                        list.add(convert(value.substring(indexes.get(i), value.length()).trim(), c));
                    } else {
                        list.add(convert(value.substring(indexes.get(i - 1), indexes.get(i)).trim(), c));
                    }
                }
            } else if (indexes.size() == 1) {
                list.add(convert(value.substring(0, indexes.get(0)).trim(), c));
                list.add(convert(value.substring(indexes.get(0) + 1, value.length()).trim(), c));
            }
        } else {
            if (value.length() > 0) {
                list.add(convert(value, c));
            }
        }
        return list;
    }

    private static <T> T convert(Object value, Class<T> c) {
        final String s = String.valueOf(value);
        Object convertedValue = null;
        if (c != null) {
            if (c.equals(String.class)) {
                convertedValue = s;
            } else if (c.equals(Integer.class)) {
                convertedValue = Integer.parseInt(s);
            } else if (c.equals(Float.class)) {
                convertedValue = Float.parseFloat(s);
            } else if (c.equals(Long.class)) {
                convertedValue = Long.parseUnsignedLong(s);
            } else if (c.equals(Boolean.class)) {
                convertedValue = Boolean.parseBoolean(s);
            }
        }
        return (T) convertedValue;
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
