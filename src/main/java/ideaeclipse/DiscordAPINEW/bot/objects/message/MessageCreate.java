package ideaeclipse.DiscordAPINEW.bot.objects.message;



import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;


public class MessageCreate extends Event implements IDiscordAction {
    private final IPrivateBot bot;
    private IMessage message;
    private IDiscordUser user;

    public MessageCreate(final IPrivateBot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize(@JsonValidity(value = {"author", "content", "id", "pinned", "channel_id"}) Json json) {
        Util.check(this, "getUser", new Json(String.valueOf(json.get("author"))));
        this.message = new Message((String) json.get("content")
                , bot.getChannels().get(Long.parseUnsignedLong(String.valueOf(json.get("channel_id"))))
                , user);
    }

    private void getUser(@JsonValidity(value = {"id"}) Json json) {
        Long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        user = bot.getUsers().get(id);
    }

    public IMessage getMessage() {
        return message;
    }
}

