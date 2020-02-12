package org.ning.proxypool.plugincore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author: NingWang
 * @since 2020/2/11.
 */
public class PluginBeanPostProcessor implements BeanPostProcessor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = bean.getClass();
        WebSitePlugin plugin = (WebSitePlugin) beanClass.getAnnotation(WebSitePlugin.class);
        if (plugin != null) {
            if (BasePlugin.class.isAssignableFrom(beanClass)) {
                PluginCache.addPlugin(beanClass);
            } else {
                logger.error("Crawler={} not extends {@link org.ning.spring_web_clone.plugin.base.BasePlugin}", beanClass.getName());
            }
        }

        return bean;
    }
}
