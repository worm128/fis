package com.fis.web.redis.base;

import com.fis.web.redis.util.JsonSerializer;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;


@Slf4j
public class JedisExecService {

    /**
     * 连接池执行类
     */
    private JedisPoolExec jedisPoolExec;
    private int dbIndex;

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    JsonSerializer jsonSerializer = new JsonSerializer();

    public void setJedisPoolExec(JedisPoolExec jedisPoolExec) {
        this.jedisPoolExec = jedisPoolExec;
    }

    public Map<String, Object> findInfoById(final String id, final String prefix) {
        final String queryId = prefix + ":" + id;
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Map<String, Object>>() {
                @Override
                public Map<String, Object> execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    String queryValue = jedis.get(queryId);
                    if (!Strings.isNullOrEmpty(queryValue)) {
                        return jsonSerializer.deserialize(queryValue);
                    }
                    return Collections.emptyMap();
                }
            });
        } catch (Exception e) {
            log.error("failed to findInfoById base Info(key={}) in base,cause:{}", queryId, Throwables.getStackTraceAsString(e));
            return Collections.emptyMap();
        }

    }

    public Boolean exists(final String id) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Boolean>() {
                @Override
                public Boolean execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    return jedis.exists(id);
                }
            });
        } catch (Exception e) {
            log.error("failed to findInfoById base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return false;
        }

    }

    public Map<String, Object> findInfoById(final String id) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Map<String, Object>>() {
                @Override
                public Map<String, Object> execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    String queryValue = jedis.get(id);
                    if (!Strings.isNullOrEmpty(queryValue)) {
                        return jsonSerializer.deserialize(queryValue);
                    }
                    return Collections.emptyMap();
                }
            });
        } catch (Exception e) {
            log.error("failed to findInfoById base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return Collections.emptyMap();
        }

    }

    public Object findInfoByIdForObj(final String id, final Class aclass) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Object>() {
                @Override
                public Object execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    String queryValue = jedis.get(id);
                    if (!Strings.isNullOrEmpty(queryValue)) {
                        if (aclass.isAssignableFrom(String.class)) {
                            return queryValue;
                        }
                        return jsonSerializer.deserializeForObject(queryValue, aclass);
                    }
                    return Collections.emptyMap();
                }
            });
        } catch (Exception e) {
            log.error("failed to findInfoById base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return Collections.emptyMap();
        }
    }

    public Object findInfoByIdForObj(final String id, final Class aclass, final Class elementClasses) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Object>() {
                @Override
                public Object execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    String queryValue = jedis.get(id);
                    if (!Strings.isNullOrEmpty(queryValue)) {
                        if (aclass.isAssignableFrom(String.class)) {
                            return queryValue;
                        }
                        return jsonSerializer.deserializeForObject(queryValue, aclass, elementClasses);
                    }
                    return Collections.emptyMap();
                }
            });
        } catch (Exception e) {
            log.error("failed to findInfoById base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return Collections.emptyMap();
        }
    }


    /**
     * 链表范围读取
     *
     * @param id
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(final String id, final int start, final int end) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<List<String>>() {
                @Override
                public List<String> execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    List<String> queryList = jedis.lrange(id, start, end);
                    if (queryList != null && queryList.size() > 0) {
                        return queryList;
                    }
                    return new ArrayList<String>();
                }
            });
        } catch (Exception e) {
            log.error("failed to lrange base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return new ArrayList<String>();
        }
    }

    /**
     * 根据索引链表读取
     *
     * @param id
     * @param index
     * @param aclass
     * @return
     */
    public Object lindex(final String id, final int index, final Class aclass) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Object>() {
                @Override
                public Object execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    String queryValue = jedis.lindex(id, index);
                    if (!Strings.isNullOrEmpty(queryValue)) {
                        if (aclass.isAssignableFrom(String.class)) {
                            return queryValue;
                        }
                        return jsonSerializer.deserializeForObject(queryValue, aclass);
                    }
                    return Collections.emptyMap();
                }
            });
        } catch (Exception e) {
            log.error("failed to lindex base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return Collections.emptyMap();
        }
    }

    public void refreshExpireTime(final String afId, final String prefix, final int maxInactiveInterval) {
        final String refreshId = prefix + ":" + afId;
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    jedis.expire(refreshId, maxInactiveInterval);
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("failed to refresh expire time session(key={}) in base,cause:{}",
                    refreshId, Throwables.getStackTraceAsString(e));
        }
    }

    public void refreshExpireTime(final String afId, final int maxInactiveInterval) {
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    jedis.expire(afId, maxInactiveInterval);
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("failed to refresh expire time session(key={}) in base,cause:{}",
                    afId, Throwables.getStackTraceAsString(e));
        }
    }


    /**
     * 通过id删除信息
     *
     * @param id
     * @return
     */
    public Long deletePhysically(final String id) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Long>() {
                @Override
                public Long execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    Long deleteCount = jedis.del(id);
                    return deleteCount;
                }
            });
        } catch (Exception e) {
            log.error("failed to delete session(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * 通过id和前缀删除信息
     *
     * @param id
     * @param prefix
     * @return
     */
    public Long deletePhysically(final String id, final String prefix) {
        final String deleteId = prefix + ":" + id;

        try {
            return this.jedisPoolExec.execute(new JedisCallback<Long>() {
                @Override
                public Long execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    Long deleteCount = jedis.del(deleteId);
                    return deleteCount;
                }
            });
        } catch (Exception e) {
            log.error("failed to delete session(key={}) in base,cause:{}", deleteId, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    public Boolean lpush(final String id, final Object snapshot) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Boolean>() {
                @Override
                public Boolean execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot == null) {
                        jedis.del(id);
                    } else {
                        jedis.lpush(id, jsonSerializer.serialize(snapshot));
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            log.error("failed to lpush base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    public Boolean rpush(final String id, final Object snapshot) {
        try {
            return this.jedisPoolExec.execute(new JedisCallback<Boolean>() {
                @Override
                public Boolean execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot == null) {
                        jedis.del(id);
                    } else {
                        jedis.rpush(id, jsonSerializer.serialize(snapshot));
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            log.error("failed to lpush base Info(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    /**
     * flush session to base
     *
     * @param id       session id
     * @param snapshot session snapshot
     */
    public boolean save(final String id, final Map<String, Object> snapshot, final int maxInactiveInterval) {
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot.isEmpty()) {
                        jedis.del(id);
                    } else {
                        jedis.setex(id, maxInactiveInterval, jsonSerializer.serialize(snapshot));
                    }
                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            log.error("failed to save session(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    public boolean save(final String id, final String prefix, final Map<String, Object> snapshot) {
        final String saveId = prefix + ":" + id;
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot.isEmpty()) {
                        jedis.del(saveId);
                    } else {
                        jedis.set(saveId, jsonSerializer.serialize(snapshot));
                    }
                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            log.error("failed to save session(key={}) in base,cause:{}", saveId, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    public boolean save(final String id, final Map<String, Object> snapshot) {
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot.isEmpty()) {
                        jedis.del(id);
                    } else {
                        jedis.set(id, jsonSerializer.serialize(snapshot));
                    }
                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            log.error("failed to save session(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return false;
        }
    }


    /**
     * 插入数据
     *
     * @param id                  id
     * @param prefix              前缀
     * @param snapshot            插入数据对象
     * @param maxInactiveInterval 有效时间
     * @return 是否插入成功
     */
    public boolean save(final String id, final String prefix, final Object snapshot, final int maxInactiveInterval) {
        final String saveId = prefix + ":" + id;
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot == null) {
                        jedis.del(saveId);
                    } else {
                        jedis.setex(saveId, maxInactiveInterval, jsonSerializer.serialize(snapshot));
                    }
                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            log.error("failed to save session(key={}) in base,cause:{}", saveId, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    /**
     * 插入数据
     *
     * @param id                  插入id号
     * @param snapshot            前缀信息
     * @param maxInactiveInterval 有效时间
     * @return 插入是否成功
     */
    public boolean save(final String id, final Object snapshot, final int maxInactiveInterval) {
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot == null) {
                        jedis.del(id);
                    } else {
                        jedis.setex(id, maxInactiveInterval, jsonSerializer.serialize(snapshot));
                    }
                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            log.error("failed to save session(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    public boolean save(final String id, final String prefix, final Object snapshot) {
        final String saveId = prefix + ":" + id;
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot == null) {
                        jedis.del(saveId);
                    } else {
                        jedis.set(saveId, jsonSerializer.serialize(snapshot));
                    }
                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            log.error("failed to save session(key={}) in base,cause:{}", saveId, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    public boolean save(final String id, final Object snapshot) {
        try {
            this.jedisPoolExec.execute(new JedisCallback<Void>() {
                @Override
                public Void execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    if (snapshot == null) {
                        jedis.del(id);
                    } else {
                        jedis.set(id, jsonSerializer.serialize(snapshot));
                    }
                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            log.error("failed to save session(key={}) in base,cause:{}", id, Throwables.getStackTraceAsString(e));
            return false;
        }
    }


    /**
     * 流量控制
     *
     * @param keyname   设置key
     * @param threshold 访问阈值
     * @param survival  生存时间
     * @return
     */
    public boolean rateControl(final String keyname, final int threshold, final int survival) {
        boolean flag = true;
        try {
            flag = this.jedisPoolExec.execute(new JedisCallback<Boolean>() {
                @Override
                public Boolean execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    //jedis.watch(keyname);
                    Transaction t = jedis.multi();
                    t.incr(keyname);
                    t.expire(keyname, survival);
                    List<Object> resultList = t.exec();
                    if (resultList != null) {
                        int count = (Integer.valueOf(resultList.get(0).toString())).intValue();
                        if (count > threshold) {
                            //System.out.println("too many requests per second : " + keyname + " : " + count );
                            log.info("too many requests per second :" + keyname + " : " + count);
                            return false;
                        } else {
                            //System.out.println("call service: " + keyname + " : " + count  );
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
            });
            return flag;
        } catch (Exception e) {
            log.error("fail to rateControl session key={} in base, cause:{}", keyname, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    /**
     * 流量控制 通过lua脚本 发送原子命令
     *
     * @param keyname   设置key
     * @param threshold 访问阈值
     * @param survive   生存时间
     * @return
     */
    public boolean rateControlToLua(final String keyname, final int threshold, final int survive) {
        boolean flag = true;
        try {
            flag = this.jedisPoolExec.execute(new JedisCallback<Boolean>() {
                @Override
                public Boolean execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    String scriptLua = "local times = base.call('incr',KEYS[1])\n"
                            + "if tonumber(times) == 1 then\n"
                            + "base.call('expire',KEYS[1],KEYS[2])\n"
                            + "end\n"
                            + "return times\n";
                    String sha = jedis.scriptLoad(scriptLua);
                    String result = jedis.evalsha(sha, 1, keyname, String.valueOf(survive)).toString();

                    int res = Integer.parseInt(result);
                    if (res > threshold) {
                        //System.out.println("too many requests per second: " + keyname + " : " + res);
                        log.info("too many requests per second :" + keyname + " : " + res);
                        return false;
                    } else {
                        //System.out.println("call service: : " + keyname + " : " + res);
                        return true;
                    }
                }
            });
            return flag;
        } catch (Exception e) {
            log.error("fail to rateControl session key={} in base, cause:{}", keyname, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    /**
     * 通过lua脚本 发送原子命令
     */
    public Object evalLua(final String luaScript, final String key1, final String key2) {
        Object flag = null;
        try {
            flag = this.jedisPoolExec.execute(new JedisCallback<Object>() {
                @Override
                public Object execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    String sha = jedis.scriptLoad(luaScript);
                    return jedis.evalsha(sha, 2, key1, key2);
                }
            });
            return flag;
        } catch (Exception e) {
            log.error("fail to evalsha to Lua in base, cause:{}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * SET if Not eXists: 如果不存在，则SET 。
     * 设置成功，返回1
     * 设置失败，返回0
     */
    public Long setnx(final String key, final String value) {
        Long res = null;
        try {
            res = this.jedisPoolExec.execute(new JedisCallback<Long>() {
                @Override
                public Long execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    return jedis.setnx(key, value);
                }
            });
            return res;
        } catch (Exception e) {
            log.error("fail to setnx in base, cause:{}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * 查找所有符合给定模式pattern 的key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(final String pattern) {
        Set<String> res = null;
        try {
            res = this.jedisPoolExec.execute(new JedisCallback<Set<String>>() {
                @Override
                public Set<String> execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    return jedis.keys(pattern);
                }
            });
            return res;
        } catch (Exception e) {
            log.error("fail to keys in base, cause:{}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * 键被正确设置过期时间时产生一个expire 通知。当EXPIREAT 设置的时
     * 间已经过期，或者EXPIRE 传入的时间为负数值或0时，键被删除，并产生一个del 通知
     *
     * @param key
     * @param seconds
     * @return 设置成功，返回1
     * 设置失败，返回0
     */
    public Long expire(final String key, final int seconds) {
        Long res = null;
        try {
            res = this.jedisPoolExec.execute(new JedisCallback<Long>() {
                @Override
                public Long execute(Jedis jedis) {
                    jedis.select(dbIndex);
                    return jedis.expire(key, seconds);
                }
            });
            return res;
        } catch (Exception e) {
            log.error("fail to expire in base, cause:{}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    public void destroy() {
        if (jedisPoolExec != null) {
            jedisPoolExec.getJedisPool().destroy();
        }
    }
}
