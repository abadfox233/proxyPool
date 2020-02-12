package org.ning.proxypool.controller;

import org.ning.proxypool.domain.Proxy;
import org.ning.proxypool.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author NingWang
 * 2020-02-12 11:55
 * 访问服务API
 */
@RestController
public class ProxyApi {

    @Autowired
    ProxyService proxyService;

    @GetMapping("/")
    Map<Object, Object> apiDocument(){
        HashMap<Object, Object> result = new HashMap<>();
        result.put("get", "随机获得一个Proxy");
        result.put("get_all", "获得全部代理");
        result.put("status", "获得系统当前状态");
        return result;
    }

    @GetMapping("/get_all")
    List<Map<String, Object>> getAllProxy(){
        List<Map<String, Object>> result = new ArrayList<>();
        for(Proxy proxy:proxyService.getAllProxy(true)){
            result.add(proxy.convert2Map());
        }
       return result;
    }

    @GetMapping("/status")
    Map<String, Object> getStatus(){
        Map<String, Object> result = new HashMap<>();
        result.put("raw Proxy Count", proxyService.proxyCount(false));
        result.put("useful Proxy Count", proxyService.proxyCount(true));
        return result;
    }
}
