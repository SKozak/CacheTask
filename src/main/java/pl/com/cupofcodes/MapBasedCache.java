package pl.com.cupofcodes;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


class MapBasedCache implements Cache {
    private static final int SHIFT_VALUE = 1;
    private final Duration ttl;
    private final Clock clock;
    private final int capacity;
    private final AtomicInteger loadedValues;
    private final Map<Key, Integer> cacheMap;
    private final OverloadStrategy overloadHandlingStrategy;

    MapBasedCache(Duration ttl, Clock clock, int capacity, OverloadStrategy overloadHandlingStrategy) {
        this.ttl = ttl;
        this.clock = clock;
        this.capacity = capacity;
        this.cacheMap = new ConcurrentHashMap<>(capacity);
        this.overloadHandlingStrategy = overloadHandlingStrategy;
        this.loadedValues = new AtomicInteger(0);
    }

    @Override
    public void put(int value) {
        if (loadedValues.incrementAndGet() > capacity) {
            loadedValues.decrementAndGet();
            overloadHandlingStrategy.handle(this);
            return;
        }
        Integer key = value + SHIFT_VALUE;
        cacheMap.put(new Key(key, clock.instant()), value);
    }

    @Override
    //Ustaliliśmy, że klient cache dba o to, żeby robić przesunięcie klucza o 1 jak usuwa, pobiera z cache
    public Integer get(int key) {
        return cacheMap.get(new Key(key, Instant.MIN));
    }

    @Override
    public String print() {
        return cacheMap.entrySet().stream()
                .map(keyIntegerEntry -> "key = %s value = %s".formatted(keyIntegerEntry.getKey(), keyIntegerEntry.getValue()))
                .reduce("", (s, s2) -> s + " \n" + s2);
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    //Ustaliliśmy, że klient cache dba o to, żeby robić przesunięcie klucza jak usuwa, pobiera z cache
    public void evict(int key) {
        cacheMap.remove(new Key(key, Instant.MIN));
    }

    @Override
    public void evictAllExpiredEntries() {
        cacheMap.entrySet().removeIf(entry -> entry.getKey().expired(clock, ttl));
    }


    @ToString
    @RequiredArgsConstructor
    private class Key {
        private final Integer keyValue;
        private final Instant instant;


        boolean expired(Clock clock, Duration ttl) {
            return this.instant.plus(ttl).isBefore(clock.instant());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            return Objects.equals(keyValue, key.keyValue);
        }

        @Override
        public int hashCode() {
            return keyValue != null ? keyValue.hashCode() : 0;
        }
    }
}
