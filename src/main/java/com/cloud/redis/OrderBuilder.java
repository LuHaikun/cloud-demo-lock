package com.cloud.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public String generateOrderNumber () {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        return simpleDateFormat.format(new Date()) + "----" + (++count);
    }
}
