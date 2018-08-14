package DiscordAPI.utils;

import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

import java.util.concurrent.*;

import static DiscordAPI.utils.DiscordUtils.HttpRequests.get;
import static DiscordAPI.utils.DiscordUtils.HttpRequests.post;
import static DiscordAPI.utils.DiscordUtils.HttpRequests.sendJson;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

public class RateLimitRecorder {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final ExecutorService service;

    public RateLimitRecorder() {
        service = Executors.newSingleThreadExecutor();
    }

    public Object queue(final IQueueHandler event) {
        QueueCaller caller = new QueueCaller(event);
        Future<Object> future = service.submit(caller);
        Object o = null;
        try {
            if (event.getClass().equals(HttpEvent.class)) {
                o = future.get();
            }
            Thread.sleep(500);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
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
            private final  JSONObject object;
            private WebSocket socket;

            public WebSocketEvent(final WebSocket socket, final JSONObject object) {
                this.socket = socket;
                this.object = object;
            }

            @Override
            public Object event() {
                socket.sendText(String.valueOf(object));
                return null;
            }
        }

        public static class HttpEvent implements IQueueHandler {
            private final RequestTypes type;
            private final String url;
            private final JSONObject object;

            public HttpEvent(final RequestTypes type, final String url, final JSONObject object) {
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
