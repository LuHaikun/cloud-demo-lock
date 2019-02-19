package com.cloud.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: luhk
 * @Email lhk2014@163.com
 * @Date: 2018/12/18 2:54 PM
 * @Description: 订单生成器
 * @Created with cloud-demo-lock
 * @Version: 1.0
 */
public class OrderBuilder {
    public static int count = 0;
    private static Lock lock = new ReentrantLock();
    public String generateOrderNumber () {
        String message;
        lock.lock();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            message = simpleDateFormat.format(new Date()) + "----" + (++count);
            System.out.println("线程名"+ count + "获得了锁");
        } finally {
            lock.unlock();
            System.out.println("线程名"+ count + "释放了锁");
        }
        return message;
    }
}
