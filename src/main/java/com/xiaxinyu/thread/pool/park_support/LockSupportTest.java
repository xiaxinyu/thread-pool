package com.xiaxinyu.thread.pool.park_support;

import java.util.concurrent.CountDownLatch;

/**
 * @author XIAXINYU3
 * @date 2021/3/8
 */
public class LockSupportTest {
    public static void startThread(String name, final FIFOMutex clock, final CountDownLatch countDownLatch) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                clock.lock();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(Thread.currentThread().getName()+"  finally");
                    countDownLatch.countDown();
                    clock.unlock();
                }
            }
        }, name).start();
    }

    public static void main(String[] args) {
        FIFOMutex clock = new FIFOMutex();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        startThread("t111", clock, countDownLatch);
        startThread("t222", clock, countDownLatch);
        startThread("t333", clock, countDownLatch);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main end");
    }
}
