package ideaeclipse.DiscordAPI.webSocket.rateLimit;

import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.customLogger.CustomLogger;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Used to be called whenever an event is ready to be executed
 * Will used the {@link #call()} method
 *
 * @author Ideaeclipse
 */
class QueueCaller implements Callable<Object> {
    private final CustomLogger logger;
    private final IQueueHandler event;

    /**
     * @param bot   discord bot
     * @param event event to execute
     */
    QueueCaller(final DiscordBot bot, final IQueueHandler event) {
        this.logger = new CustomLogger(this.getClass(), bot.getLoggerManager());
        this.event = event;
    }

    /**
     * Will print an error based on the discord documentation of bad response types
     *
     * @return the response of the event or null
     */
    @Override
    public Object call() {
        try {
            return event.event();
        } catch (IOException e) {
            String message = e.getMessage();
            message = message.replace("Server returned HTTP response code: ", "");
            int code = Integer.parseInt(message.substring(0, 3).trim());
            String url = message.replace(String.valueOf(code), "");
            switch (code) {
                case 400:
                    logger.error("Bad request" + url);
                    break;
                case 401:
                    logger.error("Unauthorized" + url + " , regenerate bot token, if that doesn't work, check https://status.discordapp.com/ for more info");
                    break;
                case 403:
                    logger.error("Forbidden" + url);
                    break;
                case 404:
                    logger.error("Directory not found" + url);
                    break;
                case 405:
                    logger.error("Method not allowed" + url);
                    break;
                case 429:
                    logger.error("The bot has made to many requests please send an issue report on github");
                    break;
                case 502:
                    logger.error("Gateway unavailable, check https://status.discordapp.com/ for more info");
                    break;
                default:
                    if (code >= 500) {
                        logger.error("Server error, check https://status.discordapp.com/ for more info");
                    } else
                        logger.error("An Unknown error has occurred" + url);
                    break;
            }
        }
        return null;
    }
}
