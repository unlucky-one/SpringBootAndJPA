package com.gc.demo.springbootandjpa.Listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/11/16
 */
public class MyHttpSessionListener implements HttpSessionListener,HttpSessionAttributeListener {
    //在线人数
    public static AtomicLong COUNT_ONLINE = new AtomicLong();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        COUNT_ONLINE.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        COUNT_ONLINE.decrementAndGet();
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

}
