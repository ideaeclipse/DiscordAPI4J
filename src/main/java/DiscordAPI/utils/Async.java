package DiscordAPI.utils;

import java.util.*;
import java.util.concurrent.*;

public class Async {

    public static <T> T queue(final IU<T> test, final String name) {
        ExecutorService service = Executors.newSingleThreadExecutor(DiscordUtils.createDaemonThreadFactory("Sync-Executor-" + name));
        Future<T> future = service.submit(new Event<>(test, 0));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> execute(final AsyncList<T> list) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<T>> future = service.submit(() -> {
            List<T> returnList = new LinkedList<>();
            List<Future<T>> eventReturnList = new LinkedList<>();
            ExecutorService s = Executors.newFixedThreadPool(list.size(), DiscordUtils.createDaemonThreadFactory("Async-Executor"));
            for (int i = 0; i < list.size(); i++) {
                Future<T> f = s.submit(new Event<>(list.get(i), i));
                eventReturnList.add(f);
            }
            for (Future<T> future1 : eventReturnList) {
                returnList.add(future1.get());
            }
            return returnList;
        });
        try {
            return future.get();
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

    public static class AsyncList<T> {
        private final List<IU<T>> list;

        public AsyncList() {
            list = new LinkedList<>();
        }

        public IU<T> get(final int i) {
            return list.get(i);
        }

        public AsyncList<T> add(final IU<T> event) {
            list.add(event);
            return this;
        }

        Integer size() {
            return list.size();
        }

    }
}
