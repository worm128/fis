package com.fis.web.redis.util;

import com.fis.web.redis.base.RedisHashService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public abstract class RedisMap<V> implements ConcurrentMap<String, V> {
    private RedisHashService redisHashService;
    private Class<V> valueClass;
    private String mapId;

    public RedisMap(String mapId, Class<V> valueClass) {
        this.mapId = mapId;
        this.valueClass = valueClass;
    }

    public void setRedisHashService(RedisHashService redisHashService) {
        this.redisHashService = redisHashService;
    }

    @Override
    public int size() {
        return redisHashService.hLen(mapId).intValue();
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return redisHashService.hExists(mapId, key.toString());
    }

    @Override
    public boolean containsValue(Object value) {
        throw new RuntimeException("Not supported operation!");
    }

    @Override
    public V get(Object key) {
        return (V) redisHashService.hGet(mapId, key.toString(), valueClass);
    }

    @Override
    public V put(String key, V value) {
        redisHashService.hSet(mapId, key, value);
        return value;
    }

    @Override
    public V remove(Object key) {
        redisHashService.hDel(mapId, key.toString());
        return null;
    }

    @Override
    public V putIfAbsent(String key, V value) {
        long count = redisHashService.hSetNx(mapId, key, value);
        if (count <= 0) {
            throw new RuntimeException(String.format("key[%s] has been exists!", key));
        }
        return value;
    }

    @Override
    public Set<String> keySet() {
        return redisHashService.hkeys(mapId);
    }

    @Deprecated
    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        throw new RuntimeException("Not supported operation!");
    }

    @Deprecated
    @Override
    public void clear() {
        throw new RuntimeException("Not supported operation!");
    }

    @Deprecated
    @Override
    public Collection<V> values() {
        throw new RuntimeException("Not supported operation!");
    }

    @Deprecated
    @Override
    public Set<Entry<String, V>> entrySet() {
        throw new RuntimeException("Not supported operation!");
    }

    @Deprecated
    @Override
    public boolean remove(Object key, Object value) {
        throw new RuntimeException("Not supported operation!");
    }

    @Deprecated
    @Override
    public boolean replace(String key, V oldValue, V newValue) {
        throw new RuntimeException("Not supported operation!");
    }

    @Deprecated
    @Override
    public V replace(String key, V value) {
        throw new RuntimeException("Not supported operation!");
    }
}
