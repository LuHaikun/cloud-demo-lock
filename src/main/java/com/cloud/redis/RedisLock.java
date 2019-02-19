package com.cloud.redis;

import org.apache.zookeeper.KeeperException;
import redis.clients.jedis.Jedis;
import java.util.Collections;
import java.util.UUID;

/**
 * @Author: luhk
 * @Email lhk2014@163.com
 * @Date: 2018/12/25 4:17 PM
 * @Description:
 * @Created with cloud-demo-lock
 * @Version: 1.0
 */
public class RedisLock {

    private Jedis jedis;
    public final String KEY = "key";
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private ThreadLocal local = new ThreadLocal();

    public RedisLock () {
        jedis = RedisUtil.getJedis();
    }

    public void acquireLock() throws InterruptedException, KeeperException {
        //创建锁节点
        if (tryLock()) {
            return;
        } else {
            Thread.sleep(500);
            acquireLock();
        }
    }

    public boolean tryLock () {
        String uuid = UUID.randomUUID().toString();
        local.set(uuid);
        String result = jedis.set(KEY, uuid, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 3000);
        if (LOCK_SUCCESS.equals(result)) {
            System.out.println(Thread.currentThread().getName() + "加锁成功！");
            return true;
        }
        return false;
    }

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 释放分布式锁
     * @return 是否释放成功
     */
    public boolean releaseLock() {
        String uuid = (String)local.get();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(KEY), Collections.singletonList(uuid));
        RedisUtil.closeResource(jedis);
        if (RELEASE_SUCCESS.equals(result)) {
            System.out.println(Thread.currentThread().getName() + ": 释放锁成功");
            return true;
        }
        return false;
    }
}
