package com.fis.web.redis.base;

import redis.clients.jedis.Jedis;


public interface JedisCallback<V> {

    V execute(Jedis jedis) ;
}
