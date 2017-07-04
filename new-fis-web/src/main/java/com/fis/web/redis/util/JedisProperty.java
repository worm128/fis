package com.fis.web.redis.util;

import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class JedisProperty {

    /**
     * maxActive 连接池最大的jedis实例个数
     */
    private String maxActive;
    /**
     * maxIdle 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
     */
    private String maxIdle;
    /**
     * timeout 控制连接池中的链接超时时间
     */
    private int timeout;

    /**
     * standAloneHost 单机版ip
     */
    private String standAloneHost;

    /**
     * standAlonePort 单机版端口
     */
    private String standAlonePort;

//    /**
//     * prefix 前缀
//     */
//    private String prefix;

    /**
     * mode 模型,cluster 集群；standAlone 单机版
     */
    private String mode;

    /**
     * masterName 集群主服务器别名
     */
    private String masterName;

    /**
     * 集群ip和port: ip:host,ipa:hosta
     */
    private String sentinelHosts;

    /**
     * masterHost 主服务器ip
     */
    private String masterHost;
    /**
     * masterPort 主服务器端口
     */
    private String masterPort;
}
