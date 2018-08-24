package DiscordAPI.utils;

import java.util.*;
import java.util.concurrent.*;

public class Async {
    private List<IU> list;

    public Async() {
        this.list = new LinkedList<>();
    }

    public <T> T queue(final IU<T> test,final String name) {
        ExecutorService service = Executors.newSingleThreadExecutor(DiscordUtils.createDaemonThreadFactory("Sync-Executor-" + name));
        Future<T> future = service.submit(new Event<>(test, 0));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> Async asyncQueue(IU<T> test) {
        list.add(test);
        return this;
    }

    public <T> List<T> execute() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<T>> future = service.submit(() -> {
            List<T> r = new LinkedList<>();
            List<Future<T>> returnList = new LinkedList<>();
            ExecutorService s = Executors.newFixedThreadPool(list.size(), DiscordUtils.createDaemonThreadFactory("Async-Executor"));
            for (int i = 0; i < list.size(); i++) {
                Future<T> f = s.submit(new Event<T>(list.get(i), i));
                returnList.add(f);
            }
            for (Future<T> future1 : returnList) {
                r.add(future1.get());
            }
            return r;
        });
        try {
            List<T> r = future.get();
            list = new LinkedList<>();
            return r;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Event<T> implements Callable<T> {
        private final IU<T> event;
        private final int threadNumber;

        Event(final IU<T> event, final int threadNumber) {
            this.threadNumber = threadNumber + 1;
            this.event = event;
        }

        @Override
        public T call() {
            Thread.currentThread().setName(Thread.currentThread().getName() + "-" + threadNumber);
            return event.update();
        }
    }

    public interface IU<T> {
        T update();
    }
}
