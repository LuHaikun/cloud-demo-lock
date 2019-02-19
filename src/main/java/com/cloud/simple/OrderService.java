package com.cloud.simple;

import java.util.ArrayList;
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
