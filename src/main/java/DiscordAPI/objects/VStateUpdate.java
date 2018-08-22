package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.Json;

public class VStateUpdate {
    private final Long user_id;
    private final Boolean suppress;
    private final String session_id;
    private final Boolean self_video;
    private final Boolean self_mute;
    private final Boolean mute;
    private final Boolean deaf;
    private final Long guild_id;
    private final Long channel_id;

    private VStateUpdate(final Long user_id, final Boolean suppress, final String session_id, final Boolean self_video, final Boolean self_mute, final Boolean mute, final Boolean deaf, final Long guild_id, final Long channel_id) {
        this.user_id = user_id;
        this.suppress = suppress;
        this.session_id = session_id;
        this.self_video = self_video;
        this.self_mute = self_mute;
        this.mute = mute;
        this.deaf = deaf;
        this.guild_id = guild_id;
        this.channel_id = channel_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Boolean getSuppress() {
        return suppress;
    }

    public String getSession_id() {
        return session_id;
    }

    public Boolean getSelf_video() {
        return self_video;
    }

    public Boolean getSelf_mute() {
        return self_mute;
    }

    public Boolean getMute() {
        return mute;
    }

    public Boolean getDeaf() {
        return deaf;
    }

    public Long getGuild_id() {
        return guild_id;
    }

    public Long getChannel_id() {
        return channel_id;
    }

    @Override
    public String toString() {
        return "{Voice State Update} User_id: " + getUser_id() + " Suppress: " + getSuppress() + " Session_Id: " + getSession_id() + " Self_video: " + getSelf_video() + " Self_mute: " + getSelf_mute() + " Mute: " + getMute() + " Deaf: " + getDeaf() + " Guild: " + getGuild_id() + " Channel: " + channel_id;
    }

    /*
    See if refactoring is possible
     */
    public static class VStateUpdateP {
        private final IDiscordBot bot;
        private final Json payload;
        private User user;
        private VStateUpdate vStateUpdate;
        private Channel channel;

        VStateUpdateP(final IDiscordBot bot, final Json payload) {
            this.bot = bot;
            this.payload = payload;
        }

        VStateUpdateP logic() {
            Payloads.DVoiceStateUpdate v = Parser.convertToPayload(payload, Payloads.DVoiceStateUpdate.class);
            vStateUpdate = new VStateUpdate(v.user_id, v.suppress, v.session_id, v.self_video, v.self_mute, v.mute, v.deaf, v.guild_id, v.channel_id);
            User.ServerUniqueUserP u = new User.ServerUniqueUserP(bot, new Json((String) payload.get("member"))).logic();
            user = u.getServerUniqueUser();
            Channel.ChannelP c = new Channel.ChannelP(vStateUpdate.getChannel_id()).logic();
            channel = c.getChannel();
            return this;
        }

        public User getUser() {
            return user;
        }

        public VStateUpdate getvStateUpdate() {
            return vStateUpdate;
        }

        public Channel getChannel() {
            return channel;
        }
    }
}
