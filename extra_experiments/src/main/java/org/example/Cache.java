package org.example;

import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V>{
    ConcurrentHashMap<K,CacheEntry<V>> cmap;

    public Cache(){
        this.cmap = new ConcurrentHashMap<K, CacheEntry<V>>();
    }

    // "put()" should always update the values, so using put() instead of putIfAbsent()
    void put(K key, V value, long ttl){
        cmap.put(key, new CacheEntry<>(value, ttl));
    }

    V get(K key){
        CacheEntry<V> val = cmap.get(key);

        if(val==null) return null;

        if(!val.hasExpired()){
            return val.getValue();
        }
        cmap.remove(key); // removing entry when key has expired
        return null;
    }

    CacheEntry<V> remove(K key){
        return cmap.remove(key);
    }
}
