package com.cyh.spring.formework.context;

/***
 * 通过解耦的方式获得IOC容器的顶层设计
 * 后面将通过一个监听器去扫描左右的类，只要实现此接口，
 * 将自动调用setApplicationContext,从而将IOC容器注入目标类中。
 */
public interface GPApplicationContextAware {

    void setApplicationContext(GPApplicationContext applicationContext);
}
