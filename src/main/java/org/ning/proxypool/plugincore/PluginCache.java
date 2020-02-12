package org.ning.proxypool.plugincore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ning Wang
 * Date 2019/12/5 11:14
 * Description
 */
public class PluginCache {

    private final static Set<Class<? extends BasePlugin>> pluginClasses = new HashSet<>();


    private final static Map<String, BasePlugin> PLUGIN_INSTANCES = new HashMap<>();

    static public void addPlugin(Class<? extends BasePlugin> baseClass){
        pluginClasses.add(baseClass);
    }

    public static Map<String, BasePlugin> getPluginInstances(){
        return PluginCache.PLUGIN_INSTANCES;
    }

    public static Set<Class<? extends BasePlugin>> getPluginClasses(){
        return PluginCache.pluginClasses;
    }

    public static void putPluginInstance(String domain, BasePlugin instance){
        PLUGIN_INSTANCES.put(domain, instance);
    }


    /**
     * 根据正则表达式匹配插件,
     * 注意： 当正则表达式匹配多个Plugin时不确定返回哪一个插件
     * @param hostRegex 网站host插件
     * @return BasePlugin
     */
    public static BasePlugin getPlugin(String hostRegex){
        BasePlugin plugin = null;
        Set<String> keys = PluginCache.PLUGIN_INSTANCES.keySet();
        for(String key:keys){
            if (key.matches(hostRegex)){
                plugin =  PLUGIN_INSTANCES.get(key);
            }
        }
        return plugin;
    }

}
