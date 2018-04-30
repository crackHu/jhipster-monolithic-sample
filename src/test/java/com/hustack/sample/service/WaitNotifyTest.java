package com.hustack.sample.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author crack
 * @date 2018/04/30
 */
public class WaitNotifyTest {

    public static void main(String[] args) {

        Object lock = new Object();
        AtomicBoolean flag = new AtomicBoolean(true);

        Runnable wait =  () -> {
            synchronized (lock) {
                while (flag.get()) {
                    System.out.println(new Date() + ":" + Thread.currentThread().getName() + " wait.");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(new Date() + ":" + Thread.currentThread().getName() + " after wait.");
            }
        };
        Runnable notify = () -> {
            synchronized (lock) {
                flag.set(false);
                System.out.println(new Date() + ":" + Thread.currentThread().getName() + " notifyAll.");
                lock.notifyAll();;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (lock) {
                System.out.println(new Date() + ":" + Thread.currentThread().getName() + " after notifyAll.");
            }
        };

        Thread waitThread = new Thread(wait, "wait thread");
        Thread notifyThread = new Thread(notify, "notify thread");

        waitThread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyThread.start();
    }

}
