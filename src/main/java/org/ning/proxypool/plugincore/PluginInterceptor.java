package org.ning.proxypool.plugincore;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author NingWang
 * 2019-12-06 15:05
 * description: 标记拦截的标识
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Component
public @interface PluginInterceptor {
}
