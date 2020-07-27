package com.cyh.spring.formework.webmvc;

import java.io.File;
import java.util.Locale;

/**
 * ViewResolver 主要完成模板名称和模板解析引擎匹配，通过在servlet中调用 resolveViewName()方法来获得模板对应的View
 *
 * 设计这个类的主要目的
 * 1、将一个静态文件转变为一个动态文件
 * 2、根据用户传送的不同参数，产生不同的结果
 * 最终输出字符串交由response 输出
 */
public class GPViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".HTML";

    private File templateRootDir;
    private String viewName;

    public GPViewResolver(String templateRoot){
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    public GPView resolveViewName(String viewName, Locale locale) throws Exception{
        this.viewName = viewName;
        if(null == viewName || "".equals(viewName.trim())){
            return null;
        }

        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);

        File templateFile = new File((templateRootDir.getPath() + "/" +viewName).replaceAll("/+","/"));

        return new GPView(templateFile);
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
