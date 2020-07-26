package com.cyh.spring.formework.beans.support;

import com.cyh.spring.formework.beans.config.GPBeanDefinition;
import com.cyh.spring.formework.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultListableBeanFactory 是众多IOC容器子类的典型代表。本次只做简单设计，定义顶层IOC缓存，就是一个Map
 */
public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    /**存储注册形象的 BeanDefinition*/
    protected final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, GPBeanDefinition>();

}
