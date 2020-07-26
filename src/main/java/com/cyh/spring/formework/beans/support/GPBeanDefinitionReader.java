package com.cyh.spring.formework.beans.support;

import com.cyh.spring.formework.beans.config.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 根据约定 BeanDefinitionReader 主要完成对 application.properties配置文件的解析工作，实现方法非常简单
 * 通过构造方法从ApplicationContext传过来的locations配置文件路径，然后解析，扫描并保存所有相关的类并提供统一的访问入口
 * 对配置文件进行查找、读取、解析
 */
public class GPBeanDefinitionReader {

    private List<String> registyBeanClass = new ArrayList<String>();
    private Properties config = new Properties();

    /**固定配置文件中的key 相对于xml的规范*/
    private final String SCAN_PACKAGE = "scanPackage";

    public GPBeanDefinitionReader(String... locations){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage){
        /**转换为文件路径，实际上就是把.替换为/*/
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));

        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()){
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String className = scanPackage + "." +file.getName().replace(".class","");
                registyBeanClass.add(className);
            }

        }
    }

    public Properties getConfig(){
        return this.config;
    }

    /**
     * 把配置文件中扫描到的所有配置信息转换为BeanDefinition 对象，以便于之后IOC 相关操作
     */
    public List<GPBeanDefinition> loadBeanDefinition(){
        List<GPBeanDefinition> result = new ArrayList<GPBeanDefinition>();
        try {
            for(String className : registyBeanClass){
                Class<?> beanClass = Class.forName(className);
                if(beanClass.isInterface()){
                    continue;
                }

                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));
                Class<?> [] interfaces = beanClass.getInterfaces();

                for(Class<?> i : interfaces){
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    private GPBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName) {
        GPBeanDefinition beanDefinition = new GPBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**首字母改为小写*/
    private String toLowerFirstCase(String simpleName){
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}
