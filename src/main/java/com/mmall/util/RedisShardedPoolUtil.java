package com.mmall.util;

import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisShardedPoolUtil {

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} exTime:{}", key, exTime, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long setNx(String key, String value){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{}", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String getSet(String key, String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getSet key:{} value:{}", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是秒
    public static String setEx(String key, String value, int exTime){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} exTime:{} value:{}", key, exTime, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String set(String key, String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{}", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{}", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{}", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {

        RedisShardedPoolUtil.set("keyTest", "valueTest");
        String value = RedisShardedPoolUtil.get("keyTest");

        RedisShardedPoolUtil.setEx("keyex", "valueex", 60*10);

        RedisShardedPoolUtil.expire("keyTest", 60*20);

        RedisShardedPoolUtil.del("keyTest");

        System.out.println("end");
    }

}
