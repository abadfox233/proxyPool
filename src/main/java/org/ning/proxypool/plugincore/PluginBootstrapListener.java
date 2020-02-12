package org.ning.proxypool.plugincore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;


public class PluginBootstrapListener implements ApplicationListener<ContextRefreshedEvent> {





    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        ApplicationContext context = event.getApplicationContext();
            if (CollectionUtils.isEmpty(PluginCache.getPluginClasses())) {
                logger.info("Not find any plugin,may be you need to check.");
                return;
            }
            for (Class<? extends BasePlugin> a : PluginCache.getPluginClasses()) {
                WebSitePlugin webSitePlugin =  (WebSitePlugin) a.getAnnotation(WebSitePlugin.class);
                String domain = webSitePlugin.domain();
                PluginCache.putPluginInstance(domain, context.getBean(a));
            }
    }
}
