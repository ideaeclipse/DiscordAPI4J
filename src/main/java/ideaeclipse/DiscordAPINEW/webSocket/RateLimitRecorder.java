package ideaeclipse.DiscordAPINEW.webSocket;

import com.neovisionaries.ws.client.WebSocket;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.JsonUtilities.Json;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder.QueueHandler.IQueueHandler;

public class RateLimitRecorder {
    private final Object lock = new Object();
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

    @SuppressWarnings("ALL")
    public Object queue(final IQueueHandler event) {
        Object o = null;
        count++;
        if (!(count > 2)) {
            QueueCaller caller = new QueueCaller(event);
            Future<Object> future = service.submit(caller);
            try {
                if (event.getClass().equals(QueueHandler.HttpEvent.class)) {
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
            put,
            delete,
            sendJson,
            sendFile
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

        @SuppressWarnings("All")
        public static class HttpEvent implements IQueueHandler {
            private final RequestTypes type;
            private final String url;
            private final Json object;
            private final String filePath;

            public HttpEvent(final RequestTypes type, final String url, final String filePath) {
                this.type = type;
                this.url = url;
                this.object = null;
                this.filePath = filePath;
            }

            public HttpEvent(final RequestTypes type, final String url, final Json object) {
                this.type = type;
                this.url = url;
                this.object = object;
                this.filePath = null;
            }

            public HttpEvent(final RequestTypes type, final String url) {
                this.type = type;
                this.url = url;
                this.object = null;
                this.filePath = null;
            }

            @Override
            public Object event() {
                switch (type) {
                    case get:
                        return Util.requests.get(url);
                    case post:
                        Util.requests.post(url);
                        break;
                    case put:
                        Util.requests.put(url);
                        break;
                    case delete:
                        Util.requests.delete(url);
                        break;
                    case sendJson:
                        assert object != null;
                        return Util.requests.sendJson(url, object);
                    case sendFile:
                        assert filePath != null;
                        return Util.requests.sendFile(url, filePath);
                }
                return null;
            }
        }

        interface IQueueHandler {
            Object event();
        }
    }
}
