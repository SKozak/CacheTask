package pl.com.cupofcodes;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@RequiredArgsConstructor
class SimpleCacheMonitor implements CacheMonitor {
    private final ScheduledExecutorService scheduler;
    private final Duration ttl;

    public static CacheMonitor of(Duration ttl) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        return new SimpleCacheMonitor(scheduler, ttl);
    }


    @Override
    public void monitor(Cache cache) {
        scheduler.scheduleAtFixedRate(cache::evictAllExpiredEntries, ttl.toMillis(), ttl.toMillis(), MILLISECONDS);
    }
}
