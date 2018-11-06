package ideaeclipse.DiscordAPI.objects.Interfaces;

public interface IReaction {
    IChannel getChannel();

    IMessage getMessage();

    String getEmoji();

    String getEmojiText();
}
