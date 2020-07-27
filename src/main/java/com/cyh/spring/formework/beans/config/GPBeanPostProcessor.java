package com.cyh.spring.formework.beans.config;

/**
 * 原生的spring中的BeanPostProcessor 是为了对象初始化事件设置的一种回调机制。
 */
public class GPBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }

}
