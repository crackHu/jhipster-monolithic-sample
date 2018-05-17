package com.hustack.sample.service.patterns;

import java.util.Observable;
import java.util.Observer;

/**
 * @author crack
 * @date 2018/05/10
 * 观察者模式
 */
public class ObserverTest {

    private static class ConcreteObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            System.out.println("------------ 👇");
            System.out.println("Observable = " + o);
            System.out.println("arg = " + arg);
            System.out.println("------------ 👆");
        }
    }

    private static class ConcreteObservable extends Observable {

        private void change() {
            super.setChanged();
        }
    }

    public static void main(String[] args) {

        ConcreteObserver concreteObserver = new ConcreteObserver();

        ConcreteObservable observable = new ConcreteObservable();
        observable.addObserver(concreteObserver);
        int count = observable.countObservers();
        observable.change();
        observable.notifyObservers("notify");
        boolean changed = observable.hasChanged();

        System.out.println("count = " + count);
        System.out.println("changed = " + changed);
    }

}
