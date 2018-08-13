package DiscordAPI.utils;

import DiscordAPI.exceptions.RateOverFlow;

import java.util.Timer;
import java.util.TimerTask;

public class RateLimitRecorder {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private Integer WssCount,HttpCount;

    public RateLimitRecorder() {
        WssCount = 0;
        HttpCount = 0;
        Timer timer = new Timer(SecondTimer.class.getSimpleName());
        timer.schedule(new SecondTimer(), 0, 1000);
    }

    public boolean updateWssCount() {
        WssCount++;
        return check(WssCount);
    }
    boolean updateHttpCount() {
        HttpCount++;
        return !check(HttpCount);
    }

    private boolean check(Integer count) {
        if (count > 5) {
            try {
                throw new RateOverFlow();
            } catch (RateOverFlow rateOverFlow) {
                logger.error("RateOverFlow");
            }
        }
        return count > 5;
    }

    private class SecondTimer extends TimerTask {

        @Override
        public void run() {
            WssCount = 0;
            HttpCount = 0;
        }
    }
}
