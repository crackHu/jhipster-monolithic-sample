package com.hustack.sample.service.patterns;

/**
 * @author crack
 * @date 2018/05/14
 * 单例模式
 */
public class SingleInstance {

    // 1. 双重检查锁
    private static class DoubleCheck {

        private static volatile DoubleCheck instance;

        private DoubleCheck() {
            System.out.println("DoubleCheck init.");
        }

        public static DoubleCheck getInstance() {
            if (instance == null) {
                synchronized (DoubleCheck.class) {
                    if (instance == null) {
                        instance = new DoubleCheck();
                    }
                }
            }
            return instance;
        }
    }

    // 2. 内部类
    private static class InnerClass {

        private InnerClass() {
            System.out.println("InnerClass init.");
        }

        private static class SingleHolder {
            private static InnerClass instance = new InnerClass();
        }

        public static InnerClass getInstance() {
            return SingleHolder.instance;
        }
    }


    public static void main(String[] args) {

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            // InnerClass.getInstance(); // 6ms
            DoubleCheck.getInstance(); // 4ms
        }
        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - begin) + "ms");
    }
}
