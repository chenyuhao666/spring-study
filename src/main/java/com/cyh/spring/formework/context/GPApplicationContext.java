package com.cyh.spring.formework.context;

import com.cyh.spring.formework.annotation.GPAutowired;
import com.cyh.spring.formework.annotation.GPController;
import com.cyh.spring.formework.annotation.GPService;
import com.cyh.spring.formework.beans.config.GPBeanDefinition;
import com.cyh.spring.formework.beans.config.GPBeanPostProcessor;
import com.cyh.spring.formework.beans.config.GPBeanWrapper;
import com.cyh.spring.formework.beans.support.GPBeanDefinitionReader;
import com.cyh.spring.formework.beans.support.GPDefaultListableBeanFactory;
import com.cyh.spring.formework.core.GPBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ApplicationContext直接接触用户的入口，主要体现DefaultListableBeanFactory 的refresh()方法 和 BeanFactory 的 getBean() 方法，
 * 完成IOC、DI、AOP 的衔接
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    private String[] configLoactions;
    private GPBeanDefinitionReader reader;

    /**单例的IOC缓存*/
    private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    private Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public GPApplicationContext(String... configLoactions){
        this.configLoactions = configLoactions;
        try {
            refresh();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void refresh() throws Exception{
        /**1.定位，定位配置文件*/
        reader = new GPBeanDefinitionReader(this.configLoactions);

        /**2.加载配置文件，扫描相关的类，把他们封装成beanDefinition*/
        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinition();

        /**3.注册，把配置信息放到容器里面(伪IOC)*/
        doRegisterBeanDefinition(beanDefinitions);

        /**4.把不是延迟加载的类提前初始化*/
        doAutowired();
    }

    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) throws Exception{
        for(GPBeanDefinition beanDefinition : beanDefinitions){
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The" + " " + beanDefinition.getFactoryBeanName() + " " + "is exists!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
    }

    /**只处理非延迟加载的情况*/
    private void doAutowired(){
        for(Map.Entry<String,GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()){
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * 依赖注入，从这里开始，读取beanDefinition中的信息
     * 然后通过反射机制创建一个实例并返回
     * spring做法是，不会把最原始的对象放出去，会用一个beanWrapper 进行一次包装
     *
     */
    @Override
    public Object getBean(String beanName) throws Exception {
        GPBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        try{
            /**完成通知事件*/
            GPBeanPostProcessor beanPostProcessor = new GPBeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if(instance == null){
                return null;
            }

            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);

            GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);

            this.factoryBeanInstanceCache.put(beanName,beanWrapper);

            beanPostProcessor.postProcessAfterInitialization(instance,beanName);

            populateBean(beanName, instance);

            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void populateBean(String beanName, Object instance) {

        Class clazz = instance.getClass();

        if(!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))){
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            if(!field.isAnnotationPresent(GPAutowired.class)){
                continue;
            }

            GPAutowired autowired = field.getAnnotation(GPAutowired.class);

            String autowiredBeanName = autowired.value().trim();

            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }

            field.setAccessible(true);

            try{
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**传入一个beanDefinition 返回一个bean实例*/
    private Object instantiateBean(GPBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try{
            /**根据class 才能确定一个类是否有实例*/
            if(this.factoryBeanObjectCache.containsKey(className)){
                instance = this.factoryBeanObjectCache.get(className);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }
}
