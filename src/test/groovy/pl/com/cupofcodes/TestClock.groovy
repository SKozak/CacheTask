package pl.com.cupofcodes


import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class TestClock extends Clock {
    private Instant instant;

    private final ZoneId zone;

    TestClock(Instant instant, ZoneId zone) {
        this.instant = instant
        this.zone = zone
    }

    static TestClock now() {
        new TestClock(Instant.now(), ZoneId.systemDefault())
    }

    @Override
    ZoneId getZone() {
        return zone;
    }

    @Override
    Clock withZone(ZoneId zone) {
        return new TestClock(instant, zone);
    }

    @Override
    Instant instant() {
        return instant;
    }

    void fastForward(Duration duration) {
        instant += duration
    }
}
