package com.cloud.lock;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: luhk
 * @Email lhk2014@163.com
 * @Date: 2018/12/18 3:00 PM
 * @Description:
 * @Created with cloud-demo-lock
 * @Version: 1.0
 */
public class OrderService implements Runnable {

    private OrderBuilder orderBuilder = new OrderBuilder();
    /**
     * 1、实现最大的并行性：有时我们想同时启动多个线程，实现最大程度的并行性。例如，我们想测试一个单例类。如果我们创建一个初始计数为1的CountDownLatch，并让所有线程都在这个锁上等待，那么我们可以很轻松地完成测试。我们只需调用 一次countDown()方法就可以让所有的等待线程同时恢复执行。
     * 2、开始执行前等待n个线程完成各自任务：例如应用程序启动类要确保在处理用户请求前，所有N个外部系统已经启动和运行了。
     * 3、死锁检测：一个非常方便的使用场景是，你可以使用n个线程访问共享资源，在每次测试阶段的线程数目是不同的，并尝试产生死锁
     */
    private static CountDownLatch countDownLatch = new CountDownLatch(50);
    private static List<String> results = new Vector<String>();
    @Override
    public void run() {
        countDownLatch.countDown();
        results.add(orderBuilder.generateOrderNumber());
    }

    public static void main(String[] args) {
        System.out.println("------开始生成订单-------");
        for (int i = 0; i < 50; i++) {
            new Thread(new OrderService()).start();
        }

        try {
            // 多个线程一直等待，直到其他线程的操作执行完后再执行
            countDownLatch.await();
            Thread.sleep(2000);
            Collections.sort(results);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i));
        }
        System.out.println("------生成订单结束-------");
    }
}
