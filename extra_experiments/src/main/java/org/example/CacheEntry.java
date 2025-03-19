package org.example;

import java.text.MessageFormat;

public class CacheEntry<V> {
    private final V value;
    private final long expiryTimeMillis;

    CacheEntry(V v, long ttl){
        this.value=v;
        this.expiryTimeMillis=System.currentTimeMillis()+ttl;
    }

    public V getValue() {
        return value;
    }

    public long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public boolean hasExpired(){
        return System.currentTimeMillis() > expiryTimeMillis;
    }

    @Override
    public String toString(){
        return MessageFormat.format("Cache Entry: {0}", value);
    }
}
