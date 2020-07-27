package com.cyh.spring.formework.webmvc;

import java.util.Map;

/**
 * ModelAndView主要用于封装页面模板和页面参数的对应关系
 */
public class GPModelAndView {

    /**页面模板名称*/
    private String viewName;
    /**往页面传送的参数*/
    private Map<String,?> model;

    public GPModelAndView(String viewName){
        this(viewName,null);
    }

    public GPModelAndView(String viewName ,Map<String,?> model){
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
