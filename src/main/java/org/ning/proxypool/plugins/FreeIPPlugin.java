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
 * Freeip
 */
@Slf4j
@WebSitePlugin(domain = "(https|http)://www\\.freeip\\.top.*")
public class FreeIPPlugin extends BasePlugin {



    @Autowired
    ProxyService proxyService;

    @Override
    public void before(Response response) {
        log.debug("处理FreeIP");
        JXDocument doc = response.document();

        List<Object> nodes = doc.sel("//tbody/tr");
        for(Object item: nodes){
            if(item instanceof Element){
                Element element = (Element)item;
                String ip = element.select("td:eq(0)").text();
                String port = element.select("td:eq(1)").text();
                proxyService.saveProxy(new Proxy(ip+":"+port, "FreeIP",  Proxy.ProxyType.getType("http")), false, true);
                log.info("get proxy {}:{} from FreeIP", ip, port);
            }
        }
    }

    @Override
    public void after(Response response) {

    }
}
