package pl.com.cupofcodes

import spock.lang.Specification

import java.time.Clock
import java.time.Duration

import static java.time.temporal.ChronoUnit.SECONDS

class CacheSpec extends Specification {


    public static final int ONE = 1
    public static final int SHIFT_VALUE = 1
    public static final int CAPACITY = 100
    private final CacheFactory cacheFactory = new CacheFactory()

    def "can create cache"() {
        given:
            Cache cache = aCache()
        expect:
            cache != null
    }

    def "can store value in cache"() {
        given:
            Cache cache = aCache()
        when:
            cache.put(ONE)
        then:
            cache.size()
    }

    def "Should add one to cache key, but value need to stay the same"() {
        given:
            Cache cache = aCache()
        when:
            cache.put(ONE)
        then:
            def cachedValue = cache.get(prepareShiftedKeyFor(ONE))
            cachedValue == 1
    }

    def "Should count cache size"() {
        given:
            Cache cache = aCache()
            cache.put(2)
            cache.put(3)
            cache.put(4)
        expect:
            cache.size() == 3
    }

    def "Should calculate cache size when element evicted"() {
        given:
            Cache cache = aCache()
            cache.put(2)
            cache.put(3)
            cache.put(4)
        when:
            cache.evict(3 + SHIFT_VALUE)
        then:
            cache.get(prepareShiftedKeyFor(3)) == null
    }

    def "Should clear only expired values"() {
        given:
            def clock = TestClock.now()
            Cache cache = aCache(Duration.of(20, SECONDS), clock)
            cache.put(2)
            cache.put(3)
            clock.fastForward(Duration.ofSeconds(40))
            cache.put(4)
        when:
            cache.evictAllExpiredEntries()
        then:
            cache.size() == 1
            cache.get(prepareShiftedKeyFor(4)) != null
    }

    def "Should handle overload based on default strategy"() {
        given:
            def clock = TestClock.now()
            Cache cache = aCache(2, Duration.of(20, SECONDS), clock)
        when:
            cache.put(2)
            cache.put(3)
            cache.put(4)
        then:
            cache.get(prepareShiftedKeyFor(4)) == null
    }


    private int prepareShiftedKeyFor(Integer key) {
        key + SHIFT_VALUE
    }


    private Cache aCache(int capacity = CAPACITY, Duration duration, Clock clock, OverloadStrategy strategy = new NoOpStrategy()) {
        cacheFactory.createCache(capacity, clock, duration, strategy)
    }

    private Cache aCache() {
        cacheFactory.createCache(CAPACITY, Clock.systemUTC(), Duration.ofSeconds(2), new NoOpStrategy())
    }
}
