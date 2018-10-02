package ideaeclipse.DiscordAPI.objects;

import ideaeclipse.DiscordAPI.utils.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.Parser;

import static ideaeclipse.DiscordAPI.utils.DiscordUtils.DefaultLinks.CHANNEL;
import static ideaeclipse.DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;

class VoiceChannel extends Channel {

    private VoiceChannel(final Long id, final String name, final Integer position, final Boolean nsfw) {
        super(id, name, position, nsfw, Payloads.ChannelTypes.voiceChannel);
    }

    static class ChannelP {
        private final Long id;
        private VoiceChannel channel;
        private Json object;

        /**
         * When calling this constructor there will be an additional API called to gain the channel Object {@link RateLimitRecorder.QueueHandler.HttpEvent}
         *
         * @param id Channel Id
         */
        ChannelP(final Long id) {
            this.id = id;
        }

        /**
         * No API call is made because you passed the channel object
         *
         * @param object channel object
         */
        ChannelP(Json object) {
            this.object = object;
            this.id = null;
        }

        /**
         * This is where the Parser parses the data you supplied
         *
         * @return returns the updated ChannelP instance
         * @see ideaeclipse.DiscordAPI.objects.Payloads.DChannel
         */
        VoiceChannel.ChannelP logic() {
            if (object == null) {
                object = new Json((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, CHANNEL + "/" + id)));
            }
            Payloads.DChannel c = ParserObjects.convertToPayload(object, Payloads.DChannel.class);
            channel = new VoiceChannel(c.id, c.name, c.position, c.nsfw);
            return this;
        }

        public VoiceChannel getChannel() {
            return channel;
        }
    }
}
