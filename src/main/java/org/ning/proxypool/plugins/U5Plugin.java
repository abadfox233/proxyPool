package org.ning.proxypool.plugins;

import cn.wanghaomiao.seimi.struct.Response;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.ning.proxypool.domain.Proxy;
import org.ning.proxypool.plugincore.BasePlugin;
import org.ning.proxypool.plugincore.WebSitePlugin;
import org.ning.proxypool.service.ProxyService;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author NingWang
 * 2020-02-11 18:12
 * 无忧代理
 */
@Slf4j
@WebSitePlugin(domain = "(https|http)://www\\.data5u\\.com.*")
public class U5Plugin extends BasePlugin {

    @Autowired
    ProxyService proxyService;

    @Override
    public void before(Response response) {
        log.debug("处理无忧代理");
        JXDocument doc = response.document();
        List<Object> nodes = doc.sel("//ul[@class='l2']");
        for(Object item: nodes){
            if(item instanceof Element){
                Element element = (Element)item;
                String ip = element.select("span:eq(0) li").text();
                String portClassName = element.select("span:eq(1) li").attr("class").split(" ")[1];
                String port = parsePort(portClassName);
                String type = element.select("span:eq(3) li").text();

                proxyService.saveProxy(new Proxy(ip+":"+port, "无忧代理",  Proxy.ProxyType.getType(type)), false, true);
                log.info("get proxy {}:{} from 无忧代理", ip, port);
            }
        }
    }

    @Override
    public void after(Response response) {

    }

    private String parsePort(String portClassName){
        int port = 0;
        String Key = "ABCDEFGHIZ";
        for(char c :portClassName.toCharArray()){
            port *= 10;
            port += Key.indexOf(c);
        }
        port >>= 3;
        return Integer.toString(port);
    }
}
