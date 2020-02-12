package org.ning.proxypool.plugincore;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Ning Wang
 * Date 2019/12/5 10:42
 * Description: web_lone 网址插件
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface WebSitePlugin {

    String domain() default "";


}
