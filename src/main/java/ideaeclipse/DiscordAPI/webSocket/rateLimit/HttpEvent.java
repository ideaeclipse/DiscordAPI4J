package ideaeclipse.DiscordAPI.webSocket.rateLimit;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.JsonUtilities.Json;

import java.io.IOException;

/**
 * This class is used with {@link RateLimitRecorder} to execute http events
 *
 * @author Ideaeclipse
 */
public class HttpEvent implements IQueueHandler {
    private final IDiscordBot bot;
    private final RequestTypes type;
    private final String url;
    private final Json object;
    private final String filePath;

    /**
     * Used when sending a file
     *
     * @param bot      discord bot
     * @param type     requestype should always be {@link RequestTypes#SENDFILE}
     * @param url      where to send file
     * @param filePath file path of the file
     */
    public HttpEvent(final IDiscordBot bot, final RequestTypes type, final String url, final String filePath) {
        this.bot = bot;
        this.type = type;
        this.url = url;
        this.object = null;
        this.filePath = filePath;
    }

    /**
     * Used when sending a json string
     *
     * @param bot    discord bot
     * @param type   should always be {@link RequestTypes#SENDJSON}
     * @param url    where to send json string
     * @param object json object
     */
    public HttpEvent(final IDiscordBot bot, final RequestTypes type, final String url, final Json object) {
        this.bot = bot;
        this.type = type;
        this.url = url;
        this.object = object;
        this.filePath = null;
    }

    /**
     * Used when doing get,put,post,delete
     *
     * @param bot  discord bot
     * @param type get,put,post,delete
     * @param url  where to go
     */
    public HttpEvent(final IDiscordBot bot, final RequestTypes type, final String url) {
        this.bot = bot;
        this.type = type;
        this.url = url;
        this.object = null;
        this.filePath = null;
    }

    /**
     * What to do with each response type
     *
     * @return return from web server
     * @throws IOException if there is an error when sending the request
     */
    @Override
    public Object event() throws IOException {
        switch (type) {
            case GET:
                return this.bot.getRequests().get(url);
            case POST:
                this.bot.getRequests().post(url);
                break;
            case PUT:
                this.bot.getRequests().put(url);
                break;
            case DELETE:
                this.bot.getRequests().delete(url);
                break;
            case SENDJSON:
                assert object != null;
                return this.bot.getRequests().sendJson(url, object);
            case SENDFILE:
                assert filePath != null;
                return this.bot.getRequests().sendFile(url, filePath);
        }
        return null;
    }
}
