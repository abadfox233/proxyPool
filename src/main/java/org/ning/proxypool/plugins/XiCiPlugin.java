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
 * 西刺代理
 */
@Slf4j
@WebSitePlugin(domain = "(https|http)://www\\.xicidaili\\.com/n[t|n]/\\d+.*")
public class XiCiPlugin extends BasePlugin {

    @Autowired
    ProxyService proxyService;

    @Override
    public void before(Response response) {
        log.debug("处理西刺代理");
        JXDocument doc = response.document();
        List<Object> nodes = doc.sel(".//table[@id='ip_list']//tr[position()>1]");
        for(Object item: nodes){
            if(item instanceof Element){
                Element element = (Element)item;
                String ip = element.select("td:eq(1)").text();
                String port = element.select("td:eq(2)").text();
                String type = ((Element) item).select("td:eq(5)").text();

                proxyService.saveProxy(new Proxy(ip+":"+port, "西刺代理",  Proxy.ProxyType.getType(type)), false, true);
                log.info("get proxy {}:{} from 西刺代理", ip, port);
            }
        }
    }

    @Override
    public void after(Response response) {

    }


}
