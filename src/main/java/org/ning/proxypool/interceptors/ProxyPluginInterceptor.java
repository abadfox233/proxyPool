package org.ning.proxypool.interceptors;

import cn.wanghaomiao.seimi.annotation.Interceptor;
import cn.wanghaomiao.seimi.core.SeimiInterceptor;
import cn.wanghaomiao.seimi.struct.Response;

import org.ning.proxypool.plugincore.BasePlugin;
import org.ning.proxypool.plugincore.PluginCache;
import org.ning.proxypool.plugincore.PluginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author NingWang
 * 2019-12-06 15:02
 * description: proxy 中对每个网站的拦截器 把成功匹配的response交由指定的插件进行处理
 */
@Interceptor(everyMethod = false)
public class ProxyPluginInterceptor implements SeimiInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Class<? extends Annotation> getTargetAnnotationClass() {
        return PluginInterceptor.class;
    }

    @Override
    public int getWeight() {
        return 8;
    }

    @Override
    public void before(Method method, Response response) {
        try{
            BasePlugin plugin = getPluginByResponse(response);
            if(plugin!=null){
                plugin.before(response);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }


    }

    @Override
    public void after(Method method, Response response) {
        try{
            BasePlugin plugin = getPluginByResponse(response);
            if(plugin!=null){
                plugin.after(response);
            }
        }catch (Exception ignore){

        }
    }

    private BasePlugin getPluginByResponse(Response response){

        try{
            if(response!=null){
                Map<String, BasePlugin> basePluginMap = PluginCache.getPluginInstances();
                for(String domain:basePluginMap.keySet()){
                    if(response.getUrl().matches(domain)){
                        return basePluginMap.get(domain);
                    }
                }
                return null;
            }else {
                return null;
            }
        }catch (Exception ignore){
            return null;
        }

    }
}
