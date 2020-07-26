package com.cyh.spring.formework.core;

/**
 * 单例工厂顶层设计
 */
public interface GPBeanFactory {

    Object getBean(String beanName) throws Exception;

    public Object getBean(Class<?> beanClass) throws Exception;

}
