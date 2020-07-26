package com.cyh.spring.formework.beans.config;

/**
 * beanWrapper主要用来封装创建后对象的实例，
 * 代理对象(proxy Object)或者原生对象(Original Object) 都有BeanWrapper 来保存
 */
public class GPBeanWrapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass;

    public GPBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance(){
        return this.wrappedInstance;
    }

    /**
     * 返回代理后的class
     * 可能是Proxy
     */
    public Class<?> getWrappedClass() {
        return this.wrappedClass;
    }
}
