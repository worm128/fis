package com.fis.web.redis.service;

import com.fis.web.redis.base.JedisExecService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JedisRateControlService {

    private JedisExecService jedisExecService;

    public void setJedisExecService(JedisExecService jedisExecService) {
        this.jedisExecService = jedisExecService;
    }
    private static String scriptLua =  "local times = base.call('incr',KEYS[1])\n"
                                        +"if tonumber(times) == 1 then\n"
                                        +"base.call('expire',KEYS[1],KEYS[2])\n"
                                        +"end\n"
                                        +"return times\n";

    public Boolean rateControlByRedisLua(String keyname, int threshold, int survival){
        Object result = jedisExecService.evalLua(scriptLua,keyname, String.valueOf(survival));
        try{
            int res = Integer.parseInt(result.toString());
            if (res > threshold){
                log.info("too many requests per second :"+ keyname + " : " + res);
                return false;
            }else {
                return true;
            }
        }catch (Exception e ){
            log.error("fail to evalsha to Lua in base, cause:{}", Throwables.getStackTraceAsString(e));
        }
        return false;
    }
}
