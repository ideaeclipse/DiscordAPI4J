package DiscordAPI.utils;

import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static DiscordAPI.utils.DiscordUtils.HttpRequests.get;
import static DiscordAPI.utils.DiscordUtils.HttpRequests.post;
import static DiscordAPI.utils.DiscordUtils.HttpRequests.sendJson;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

public class RateLimitRecorder {
    private final Object lock = new Object();
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final ExecutorService service;
    private Integer count;

    public RateLimitRecorder() {
        service = Executors.newSingleThreadExecutor();
        count = 0;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count = 0;
                synchronized (lock) {
                    lock.notify();
                }
            }
        }, 0, 1000);
    }

    public Object queue(final IQueueHandler event) {
        Object o = null;
        count++;
        if (!(count > 2)) {
            QueueCaller caller = new QueueCaller(event);
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

    private class QueueCaller implements Callable<Object> {
        private final IQueueHandler event;

        private QueueCaller(final IQueueHandler event) {
            this.event = event;
        }

        @Override
        public Object call() {
            return event.event();
        }
    }

    public static class QueueHandler {
        public enum RequestTypes {
            get,
            post,
            sendJson
        }

        public static class WebSocketEvent implements IQueueHandler {
            private final Json object;
            private final WebSocket socket;

            public WebSocketEvent(final WebSocket socket, final Json object) {
                this.socket = socket;
                this.object = object;
            }

            @Override
            public Object event() {
                socket.sendText(object.toString());
                return null;
            }
        }

        public static class HttpEvent implements IQueueHandler {
            private final RequestTypes type;
            private final String url;
            private final Json object;

            public HttpEvent(final RequestTypes type, final String url, final Json object) {
                this.type = type;
                this.url = url;
                this.object = object;
            }

            public HttpEvent(final RequestTypes type, final String url) {
                this.type = type;
                this.url = url;
                this.object = null;
            }

            @Override
            public Object event() {
                switch (type) {
                    case get:
                        return get(url);
                    case post:
                        post(url);
                        break;
                    case sendJson:
                        return sendJson(url, object);
                }
                return null;
            }
        }

        interface IQueueHandler {
            Object event();
        }
    }
}
