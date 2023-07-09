package pl.com.cupofcodes;

import java.time.Clock;
import java.time.Duration;

class CacheFactory {

    public Cache createCache(int capacity, Duration ttl) {
        return createCache(capacity, Clock.systemUTC(), ttl, new NoOpStrategy());
    }

    public Cache createCache(int capacity, Clock clock, Duration ttl, OverloadStrategy overloadHandlingStrategy) {
        return new MapBasedCache(ttl, clock, capacity, overloadHandlingStrategy);
    }

}
