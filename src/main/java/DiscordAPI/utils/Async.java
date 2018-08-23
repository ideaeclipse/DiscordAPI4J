package DiscordAPI.utils;

import DiscordAPI.IDiscordBot;

import java.util.*;
import java.util.concurrent.*;

public class Async {
    private final IDiscordBot bot;
    private List<IU> list;

    public Async(final IDiscordBot bot) {
        this.bot = bot;
        this.list = new LinkedList<>();
    }

    public List queue(IU test) {
        ExecutorService service = Executors.newSingleThreadExecutor(DiscordUtils.createDaemonThreadFactory(String.valueOf(test.getClass().getSimpleName())));
        Future<List> future = service.submit(new Event(test, 0));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void asyncQueue(IU test) {
        list.add(test);
    }

    public List execute() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List> future = service.submit(() -> {
            List<List> r = new LinkedList<>();
            List<Future<List>> returnList = new LinkedList<>();
            ExecutorService s = Executors.newFixedThreadPool(list.size(), DiscordUtils.createDaemonThreadFactory("Async-Executor"));
            for (int i = 0; i < list.size(); i++) {
                Future<List> f = s.submit(new Event(list.get(i), i));
                returnList.add(f);
            }
            for (Future<List> future1 : returnList) {
                r.add(future1.get());
            }
            return r;
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Event implements Callable<List> {
        private final IU event;
        private final int threadNumber;

        Event(final IU event, final int threadNumber) {
            this.threadNumber = threadNumber + 1;
            this.event = event;
        }

        @Override
        public List call() {
            Thread.currentThread().setName(Thread.currentThread().getName() + "-" + threadNumber);
            return event.update();
        }
    }

    public interface IU {
        List update();
    }
}
