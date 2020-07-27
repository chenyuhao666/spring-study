package com.cyh.spring.formework.webmvc.servlet;

import com.cyh.spring.formework.context.GPApplicationContext;
import com.cyh.spring.formework.webmvc.GPHandlerAdapter;
import com.cyh.spring.formework.webmvc.GPHandlerMapping;
import com.cyh.spring.formework.webmvc.GPViewResolver;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet 作为mvc的入口
 */
@Slf4j
public class GPDispatcherServlet extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";

    /**handlerMappings最核心的设计*/
    private List<GPHandlerMapping> handlerMappings = new ArrayList<>();

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapters = new HashMap<>();

    private List<GPViewResolver> viewResolvers = new ArrayList<>();

    private GPApplicationContext context;

    /**
     * 下面实现init方法,主要完成容器初始化 和springMVC九大组件初始化
     */
    @Override
    public void init(ServletConfig config)throws ServletException{
        context = new GPApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(GPApplicationContext context) {
        /**
         * 有九种策略
         * 针对每个用户的请求，都会经过一些处理策略处理，最终才能有结果输出
         * 每种策略可以自定义干预，但是最终的结果是一致
         */
        /**
         * 文件上传解析类
         */
        initMultipartResolvers(context);
        /**
         * 本地化解析
         */
        initLocalResolvers(context);
        /**
         * 主体解析
         */
        initThemeResolvers(context);
        /**
         * 自己实现HandlerMapping
         */
        initHandlerMappings(context);
        /**
         * 自己实现HandlerAdapter
         */
        initHandlerAdapters(context);
        /**
         * 执行过程中遇到异常，交给handlerExceptionResolver
         */
        initHandlerExceptionResolvers(context);
        /**
         * 直接将请求解析到视图名
         */
        initRequestToViewNameTranslator(context);
        /**
         * 自己实现 实现动态模板的解析
         */
        initViewResolvers(context);
        /**
         * Flash 映射管理器
         */
        initFlashManager(context);
    }

    private void initFlashManager(GPApplicationContext context) {
    }

    private void initViewResolvers(GPApplicationContext context) {
        /**
         * 页面中输入http://localhost/first.html
         */
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);

        for(File template : templateRootDir.listFiles()){
            this.viewResolvers.add(new GPViewResolver(template.getPath()));
        }
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    private void initHandlerAdapters(GPApplicationContext context) {
        /**
         * 初始化阶段，我们能做的是就是将参数名字或者类型按照一定的顺序保存下来
         * 因为后面用反射调用的时候，传的形参是一个数组
         * 可以通过记录这些参数的位置index,逐个从数组中取值，这样就和参数顺序无关了
         */
        for(GPHandlerMapping handlerMapping : this.handlerMappings){
            this.handlerAdapters.put(handlerMapping, new GPHandlerAdapter());
        }
    }

    private void initHandlerMappings(GPApplicationContext context) {
    }

    private void initThemeResolvers(GPApplicationContext context) {
    }

    private void initLocalResolvers(GPApplicationContext context) {
    }

    private void initMultipartResolvers(GPApplicationContext context) {
    }

}
