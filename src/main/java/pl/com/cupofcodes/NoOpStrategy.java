package pl.com.cupofcodes;

class NoOpStrategy implements OverloadStrategy {
    @Override
    public void handle(Cache cache) {
        // No-Op strategy
    }
}
