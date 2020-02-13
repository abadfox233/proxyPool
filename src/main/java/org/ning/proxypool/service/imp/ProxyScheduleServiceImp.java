package org.ning.proxypool.service.imp;

import cn.wanghaomiao.seimi.spring.common.CrawlerCache;
import cn.wanghaomiao.seimi.struct.CrawlerModel;
import cn.wanghaomiao.seimi.struct.Request;
import lombok.extern.slf4j.Slf4j;
import org.ning.proxypool.domain.Proxy;
import org.ning.proxypool.service.ProxyCheckService;
import org.ning.proxypool.service.ProxyScheduleService;
import org.ning.proxypool.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author NingWang
 * 2020-02-11 22:14
 */
@Service
@Slf4j
public class ProxyScheduleServiceImp implements ProxyScheduleService {

    @Autowired
    ProxyService proxyService;
    @Autowired
    ProxyCheckService  proxyCheckService;


    @Override
    public void getProxy() {
        log.debug("开始获取代理");
        CrawlerModel proxy_crawler = CrawlerCache.getCrawlerModel("proxy_crawler");

        proxy_crawler.sendRequest(Request.build("http://www.data5u.com/", "start"));

        proxy_crawler.sendRequest(Request.build("http://www.goubanjia.com/", "start"));

        proxy_crawler.sendRequest(Request.build("https://www.kuaidaili.com/free/inha/", "start"));

        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException ignore){

        }
        proxy_crawler.sendRequest(Request.build("https://www.kuaidaili.com/free/intr/", "start"));

        proxy_crawler.sendRequest(Request.build("http://www.ip3366.net/free/?stype=1", "start"));
        proxy_crawler.sendRequest(Request.build("http://www.ip3366.net/free/?stype=2", "start"));

        proxy_crawler.sendRequest(Request.build("http://www.iphai.com/free/ng", "start"));
        proxy_crawler.sendRequest(Request.build("http://www.iphai.com/free/np", "start"));
        proxy_crawler.sendRequest(Request.build("http://www.iphai.com/free/wg", "start"));
        proxy_crawler.sendRequest(Request.build("http://www.iphai.com/free/wp", "start"));

        proxy_crawler.sendRequest(Request.build("https://www.freeip.top/", "start"));

        String xiCi1 = "https://www.xicidaili.com/nt/";
        String xiCi2 = "https://www.xicidaili.com/nn/";
        for(int i=1;i<=10;i++){
            proxy_crawler.sendRequest(Request.build(xiCi1 + i, "start"));
            proxy_crawler.sendRequest(Request.build(xiCi2 + i, "start"));
            try {
                TimeUnit.SECONDS.sleep(5);
            }catch (InterruptedException ignore){

            }
        }


    }

    @Override

    public void checkProxy() {
        log.debug("开始执行检测");
       dispatchProxy2Check(true);
       dispatchProxy2Check(false);

    }

    private void dispatchProxy2Check(boolean check){
        Set<Proxy> tempProxySet = new HashSet<>();
        Set<Proxy> proxySet = proxyService.getAllProxy(check);
        for(Proxy proxy:proxySet){
            if(tempProxySet.size()==30){
                proxyCheckService.checkProxy(tempProxySet, check);
                tempProxySet = new HashSet<>();
            }else {
                tempProxySet.add(proxy);
            }
        }
        if(tempProxySet.size()>0){
            proxyCheckService.checkProxy(tempProxySet, check);
//            tempProxySet = new HashSet<>();
        }
    }
}
