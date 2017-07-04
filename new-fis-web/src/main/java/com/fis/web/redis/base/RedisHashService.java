package com.fis.web.redis.base;

import com.fis.web.redis.util.JsonSerializer;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;


@Slf4j
public class RedisHashService {
    private JedisPoolExec jedisPoolExec;

    JsonSerializer jsonSerializer = new JsonSerializer();

    public void setJedisPoolExec(JedisPoolExec jedisPoolExec) {
        this.jedisPoolExec = jedisPoolExec;
    }

    public Object hGet(final String mapId, final String field, final Class<?> valueClass) {
        return this.jedisPoolExec.execute(new JedisCallback<Object>() {
            @Override
            public Object execute(Jedis jedis) {
                String strValue = jedis.hget(mapId, field);
                if (!Strings.isNullOrEmpty(strValue) && !strValue.equals("[]")) {
                    return jsonSerializer.deserializeForObject(strValue, valueClass);
                }
                return null;
            }
        });
    }

    public long hSet(final String mapId, final String field, final Object value) {
        return this.jedisPoolExec.execute(new JedisCallback<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                String strValue = jsonSerializer.serialize(value);
                return jedis.hset(mapId, field, strValue);
            }
        });
    }

    public Long hSetNx(final String mapId, final String field, final Object value) {
        return this.jedisPoolExec.execute(new JedisCallback<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                String strValue = jsonSerializer.serialize(value);
                return jedis.hsetnx(mapId, field, strValue);
            }
        });
    }

    public Boolean hExists(final String mapId, final String field) {
        return this.jedisPoolExec.execute(new JedisCallback<Boolean>() {
            @Override
            public Boolean execute(Jedis jedis) {
                return jedis.hexists(mapId, field);
            }
        });
    }

    public Map<String, String> hGetAll(final String mapId) {
        return this.jedisPoolExec.execute(new JedisCallback<Map<String, String>>() {
            @Override
            public Map<String, String> execute(Jedis jedis) {
                return jedis.hgetAll(mapId);
            }
        });
    }

    public void hDel(final String mapId, final String field) {
        this.jedisPoolExec.execute(new JedisCallback<Void>() {
            @Override
            public Void execute(Jedis jedis) {
                jedis.hdel(mapId, field);
                return null;
            }
        });
    }

    public Long hLen(final String mapId) {
        return this.jedisPoolExec.execute(new JedisCallback<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.hlen(mapId);
            }
        });
    }

    public Set<String> hkeys(final String mapId) {
        return this.jedisPoolExec.execute(new JedisCallback<Set<String>>() {
            @Override
            public Set<String> execute(Jedis jedis) {
                return jedis.hkeys(mapId);
            }
        });
    }
}
