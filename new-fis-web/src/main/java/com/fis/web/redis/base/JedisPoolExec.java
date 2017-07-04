package com.fis.web.redis.base;

import com.fis.web.redis.util.JedisProperty;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

import javax.annotation.PostConstruct;
import java.util.Set;

@Slf4j
public class JedisPoolExec {

    /**
     * 属性配置信息
     */
    private JedisProperty jedisProperty;

    /**
     * redis连接池
     */
    private volatile Pool<Jedis> jedisPool;

    private String masterHost;
    private int masterPort;

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    public int getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    /**
     * 执行方法
     * @param cb
     * @param <V>
     * @return
     */
    public <V> V execute(JedisCallback<V> cb) {
        Jedis jedis = null;
        boolean success = true;
        try {
            jedis = jedisPool.getResource();
            return cb.execute(jedis);
        } catch (JedisException e) {
            success = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            throw e;
        }finally {
            if (success) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 初始化信息
     */
    @PostConstruct
    public void initJedisPoolExec() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        if(StringUtils.isEmpty(jedisProperty.getMaxActive())){
//            config.setMaxActive(200);
            //jedis.version>2.2.1 maxActive  ==>  maxTotal
            config.setMaxTotal(200);
        }else {
//            config.setMaxActive(Integer.parseInt(jedisProperty.getMaxActive()));
            config.setMaxTotal(Integer.parseInt(jedisProperty.getMaxActive()));
        }
        if(StringUtils.isEmpty(jedisProperty.getMaxIdle())){

            config.setMaxIdle(10);
        }else {
            config.setMaxIdle(Integer.parseInt(jedisProperty.getMaxIdle()));

        }
        //config.setMaxWait(1000l);
        //jedis.version>2.2.1 maxWait  ==>  maxWaitMillis
        config.setMaxWaitMillis(1000l);

        if(!StringUtils.isEmpty(jedisProperty.getMasterName())){
            String sentinelProps = jedisProperty.getSentinelHosts();
            Iterable<String> parts = Splitter.on(',').trimResults().omitEmptyStrings().split(sentinelProps);

            final Set<String> sentinelHosts = Sets.newHashSet(parts);
            String masterName = jedisProperty.getMasterName();
            //System.out.println("masterName=[" + masterName + "],sentinelHosts=[" + sentinelHosts + "],config=[" + config + "]");
            this.jedisPool = new JedisSentinelPool(masterName,sentinelHosts,config,jedisProperty.getTimeout());

        }else{
            String redisHost = jedisProperty.getStandAloneHost();
            int redisPort = Integer.parseInt(jedisProperty.getStandAlonePort());
            this.jedisPool = new JedisPool(config, redisHost, redisPort);
        }
    }

    public void setJedisProperty(JedisProperty jedisProperty) {
        this.jedisProperty = jedisProperty;
    }
}
