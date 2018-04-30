package com.hustack.sample.service;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author crack
 * @date 2018/04/30
 */
public class LockTest {

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();


    public static void main(String[] args) {
        Runnable run = () -> {
            try {
                lock.lock();
                for (int i = 0; i < 5; i++) {
                    System.out.println("ThreadName=" + Thread.currentThread().getName()
                        + (" " + (i + 1)));
                }
            } finally {
                lock.unlock();
            }
        };

        new Thread(run).start();
        new Thread(run).start();
    }
}
