package com.hustack.sample.service.patterns;

import java.util.Observable;
import java.util.Observer;

/**
 * @author crack
 * @date 2018/05/10
 * 观察者模式
 */
public class ObserverTest {

    public class AbstractObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {

        }
    }
}
