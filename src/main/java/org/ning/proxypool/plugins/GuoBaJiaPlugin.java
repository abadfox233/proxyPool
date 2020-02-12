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
@WebSitePlugin(domain = "(https|http)://www\\.goubanjia\\.com.*")
public class GuoBaJiaPlugin extends BasePlugin {

    @Autowired
    ProxyService proxyService;

    @Override
    public void before(Response response) {
        log.debug("处理GuoBaJia代理");
        JXDocument doc = response.document();
        List<Object> nodes = doc.sel("//td[@class='ip']");
        for(Object item: nodes){
            if(item instanceof Element){
                Element element = (Element)item;
                String ip = String.join("",element.select("td > *:not(.port):not([style*='display: none'])").text().trim()).replace(" ", "");
                String portClass = element.select(".port").attr("class").split(" ")[1];
                String port = parsePort(portClass);

                proxyService.saveProxy(new Proxy(ip+":"+port, "GuaBaJia代理",  Proxy.ProxyType.getType("http")), false, true);
                log.info("get proxy {}:{} from GuaBaJia代理", ip, port);
            }
        }
    }

    @Override
    public void after(Response response) {

    }

    private String parsePort(String portClassName){
        String KEY = "ABCDEFGHIZ";
        int port = 0;
        for(char c : portClassName.toCharArray()){
            port *= 10;
            port += KEY.indexOf(c);
        }
        return Integer.toString(port>>3);
    }
}
