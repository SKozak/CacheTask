package pl.com.cupofcodes

import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Duration

class SimpleCacheMonitorSpec extends Specification {
    public static final int CAPACITY = 100

    def "Should monitor cache and remove expired entries"() {
        given:
            def pollingConditions = new PollingConditions(initialDelay: 0, delay: 0.2, timeout: 1)
            def timeToLeave = Duration.ofMillis(100)
            def cache = aCache(timeToLeave)
            CacheMonitor cacheMonitor = SimpleCacheMonitor.of(timeToLeave);
            cacheMonitor.monitor(cache);
            cache.put(1)
            cache.put(2)
            cache.put(5)
        expect:
            pollingConditions.eventually {
                cache.size() == 0
            }
    }

    Cache aCache(Duration duration) {
        CacheFactory cacheFactory = new CacheFactory()
        cacheFactory.createCache(CAPACITY, duration)
    }
}
