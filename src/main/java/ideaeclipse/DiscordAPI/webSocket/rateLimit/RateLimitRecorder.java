package ideaeclipse.DiscordAPI.webSocket.rateLimit;

import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;

import java.util.Optional;
import java.util.concurrent.*;

/**
 * TODO: separate rate limits per directory not overall
 * <p>
 * Used to execute {@link IQueueHandler} events. Will only execute them with accordance to Discords rate limit policy
 *
 * @author Ideaeclipse
 */
public final class RateLimitRecorder {
    private final Object lock = new Object();
    private final IDiscordBot bot;
    private final ExecutorService service;
    private Integer count;

    /**
     * @param bot discord bot
     */
    public RateLimitRecorder(final IDiscordBot bot) {
        this.bot = bot;
        service = Executors.newSingleThreadExecutor();
        count = 0;
        Async.addJob(o -> {
            count = 0;
            synchronized (lock) {
                lock.notify();
            }
            return Optional.empty();
        }, 1000);
    }

    /**
     * if counts is greater than 2 wait until the async job resets the count ever second, then execute event
     * @param event event to execute
     * @return
     */
    @SuppressWarnings("ALL")
    public Object queue(final IQueueHandler event) {
        Object o = null;
        count++;
        if (!(count > 2)) {
            QueueCaller caller = new QueueCaller(this.bot, event);
            Future<Object> future = service.submit(caller);
            try {
                if (event.getClass().equals(HttpEvent.class)) {
                    o = future.get();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                synchronized (lock) {
                    lock.wait();
                    return queue(event);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return o;
    }
}
