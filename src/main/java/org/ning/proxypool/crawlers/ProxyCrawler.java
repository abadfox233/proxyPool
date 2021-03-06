package org.ning.proxypool.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.SeimiHttpType;
import cn.wanghaomiao.seimi.struct.Response;
import lombok.extern.slf4j.Slf4j;
import org.ning.proxypool.plugincore.PluginInterceptor;

/**
 * @author NingWang
 * 2020-02-11 12:41
 * 爬取代理框架爬虫
 */
@Slf4j
@Crawler(name = "proxy_crawler", delay = 3, useUnrepeated = false, httpType = SeimiHttpType.OK_HTTP3)
public class ProxyCrawler extends BaseSeimiCrawler {

    @Override
    public String[] startUrls() {

        return new String[]{

        };
    }

    @Override
    @PluginInterceptor
    public void start(Response response) {

    }
}
