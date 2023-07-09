package pl.com.cupofcodes;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;

@Slf4j
class CacheApplication {

    private static final int CAPACITY = 100;

    public static void main(String[] args) throws InterruptedException {
        final CacheFactory cacheFactory = new CacheFactory();
        final Duration ttl = Duration.of(CAPACITY, MILLIS);
        Cache cache = cacheFactory.createCache(CAPACITY, ttl);
        CacheMonitor cacheMonitor = SimpleCacheMonitor.of(ttl);
        cacheMonitor.monitor(cache);
        cache.put(2);
        cache.put(3);
        cache.put(4);
        cache.put(5);

        log.info("cache values {}", cache.print());
        Thread.sleep(ttl.toMillis());
        cache.put(8);
        log.info("cache values {}", cache.print());
    }
}
