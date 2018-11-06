package ideaeclipse.DiscordAPI.objects;

import emoji4j.EmojiUtils;
import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.Interfaces.IMessage;
import ideaeclipse.DiscordAPI.objects.Interfaces.IReaction;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.JsonUtilities.Json;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Reaction implements IReaction {
    private final IChannel channel;
    private final IMessage message;
    private final String emoji, emojiText;

    Reaction(final IChannel channel, final IMessage message, final String emoji) {
        this.channel = channel;
        this.message = message;
        this.emoji = emoji;
        this.emojiText = EmojiUtils.shortCodify(emoji);
    }

    @Override
    public IChannel getChannel() {
        return this.channel;
    }

    @Override
    public IMessage getMessage() {
        return this.message;
    }

    @Override
    public String getEmoji() {
        return this.emoji;
    }

    @Override
    public String getEmojiText() {
        return this.emojiText;
    }

    static class ReactionP {
        private final IPrivateBot bot;
        private final Json json;
        private IReaction reaction;

        ReactionP(final IPrivateBot bot, final Json json) {
            this.bot = bot;
            this.json = json;
        }

        ReactionP logic() {
            Payloads.DReactionAdd r = ParserObjects.convertToPayload(json, Payloads.DReactionAdd.class);
            Payloads.DEmoji e = ParserObjects.convertToPayload(r.emoji, Payloads.DEmoji.class);
            IChannel channel = bot.getChannels().stream().filter(o -> o.getId().equals(r.channel_id)).collect(Collectors.toList()).get(0);
            List<IMessage> message = channel.messageHistory().stream().filter(o -> o.getMessageId().equals(r.message_id)).collect(Collectors.toList());
            this.reaction = new Reaction(channel, message.isEmpty() ? null : message.get(0), e.name);
            return this;
        }

        public IReaction getReaction() {
            return this.reaction;
        }
    }
}
