package com.cyh.spring.formework.context.support;

/**
 * IOC 容器实现类的顶层抽象类，实现IOC容器相关的公共逻辑。为了尽可能简化，MINI版本暂时只设计一个refresh()方法
 */
public abstract class GPAbstractApplicationContext {

    /**受保护 只提供给子类重写*/
    protected void refresh() throws Exception {}
}
