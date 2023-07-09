package pl.com.cupofcodes;

interface Cache {
    void put(int key);

    Integer get(int key);

    String print();

    int size();

    void evict(int key);

    void evictAllExpiredEntries();
}
