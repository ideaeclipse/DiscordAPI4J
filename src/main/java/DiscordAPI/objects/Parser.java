package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Parser {
    public static class CC {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Channel channel;

        public CC(final IDiscordBot b, final JSONObject payload) {
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

    public static class CD {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Channel channel;

        public CD(final IDiscordBot b, final JSONObject payload) {
            Channel.ChannelP cd = new Channel.ChannelP(payload).logic();
            channel = cd.getChannel();
            b.updateChannels();
            logger.info("Channel Delete: Channel Name: " + channel.getName() + " NSFW: " + channel.getNsfw() + " Position: " + channel.getPosition());
        }

        public Channel getChannel() {
            return channel;
        }
    }

    public static class CU {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Channel oldC;
        private volatile Channel newC;

        public CU(final IDiscordBot b, final JSONObject payload) {
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

    public static class MC {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Message message;

        public MC(final IDiscordBot b, final JSONObject object) {
            JSONObject user = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(object.get("author")));
            Payloads.DUser u = convertToJSON(user, Payloads.DUser.class);
            Payloads.DMessage m = convertToJSON(object, Payloads.DMessage.class);
            User.UserP pd = new User.UserP(u.id, b).logic();
            Channel.ChannelP cd = new Channel.ChannelP(m.channel_id).logic();
            message = new Message(pd.getUser(), cd.getChannel(), m.guild_id, m.content);
            if (message.getChannel().getType().equals(Payloads.ChannelTypes.textChannel)) {
                logger.info("Message Create: User: " + message.getUser().getName() + " Content: " + message.getContent() + " Channel: " + message.getChannel().getName());
            } else {
                logger.info("Dm Sent: User: " + message.getUser().getName() + " Content: " + message.getContent());
            }
        }


        public Message getMessage() {
            return this.message;
        }
    }

    public static class PU {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private volatile Status status;

        public PU(final IDiscordBot t, final JSONObject payload) {
            final Payloads.DUser user = convertToJSON((JSONObject) Objects.requireNonNull(DiscordUtils.convertToJSONOBJECT(String.valueOf(payload.get("user")))),Payloads.DUser.class);
            final User.UserP pd = new User.UserP(user.id, t).logic();
            final Game.GameP gd = payload.get("game") != null ? new Game.GameP((JSONObject) payload.get("game")).logic() : null;
            status = new Status(gd != null ? gd.getGame() : null, pd.getUser(), String.valueOf(payload.get("status")));
            logger.info("Presence Update: User: " + status.getUser().getName() + " Status: " + status.getStatus() + (status.getGame() != null ? " Game: " + ((status.getGame().getType() == Payloads.GameTypes.Playing) ?
                    "Playing " + status.getGame().getName() + " Details: " + status.getGame().getState() + " " + status.getGame().getDetails()
                    : "Listening to " + status.getGame().getState() + " Song: " + status.getGame().getDetails() + " on " + status.getGame().getName()) : ""));
        }

        public Status getStatus() {
            return status;
        }
    }

    public static <T> T convertToJSON(final JSONObject object, final Class<?> c) {
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
                String value = String.valueOf(object.get(s));
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
    /*
    Refactor to not use static class indexing
     */
    private static <T> T getObject(final Class<?> c) {
        T o = null;
        try {
            if (c.getName().contains("$")) {
                Class<?> a = Payloads.class;
                Object superC = a.getConstructor().newInstance();
                o = (T) c.getConstructor(superC.getClass()).newInstance(superC);
            } else {
                o = (T) c.getConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return o;
    }
}
