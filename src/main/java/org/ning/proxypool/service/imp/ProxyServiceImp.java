package org.ning.proxypool.service.imp;

import lombok.extern.slf4j.Slf4j;
import org.ning.proxypool.domain.Proxy;
import org.ning.proxypool.service.ProxyService;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author NingWang
 * 2020-02-11 17:36
 * 负责对代理的存储删除操作
 */
@Slf4j
@Service
@DependsOn("proxyRedissonClient")
public class ProxyServiceImp implements ProxyService {


    private RedissonClient redissonClient;

    private RSet<String> filterSet = null;
    private RSet<Proxy> rawProxySet = null;
    private RSet<Proxy> usefulProxySet = null;

    public ProxyServiceImp(@Qualifier("proxyRedissonClient") RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        filterSet =redissonClient.getSet("filterSet");
        rawProxySet = redissonClient.getSet("rawProxySet");
        usefulProxySet = redissonClient.getSet("usefulProxySet");
    }


    /**
     * 保存一个代理
     *
     * @param proxy 代理
     * @param check 是否校验
     * @param filter 是否去重
     */
    @Override
    public void saveProxy(Proxy proxy, boolean check, boolean filter) {
        if(filter){
            if(filterSet.contains(proxy.getProxyStr())) {
                log.debug("filter proxy {}", proxy);
                return;
            }
        }
        filterSet.add(proxy.getProxyStr());
        if(check){
            usefulProxySet.add(proxy);
        }else {
            rawProxySet.add(proxy);
        }


    }

    /**
     * 代理总数统计
     *
     * @return void
     */
    @Override
    public int proxyCount(boolean check) {
        return check?usefulProxySet.size():rawProxySet.size();
    }

    /**
     * 去除一个代理
     *
     * @param proxy 代理
     * @param check 是否校验
     */
    @Override
    public void removeProxy(Proxy proxy, boolean check) {
        if(filterSet.contains(proxy.getProxyStr())){
            if(check){
                usefulProxySet.remove(proxy);
            }else {
                rawProxySet.remove(proxy);
            }
        }
    }

    /**
     * 随机获取一个代理
     *
     * @param check 是否校验
     * @return Proxy
     */
    @Override
    public Proxy getProxy(boolean check) {
        return check?usefulProxySet.random():rawProxySet.random();
    }

    /**
     * 获取全部代理
     *
     * @param check 是否校验
     * @return 代理集合
     */
    @Override
    public Set<Proxy> getAllProxy(boolean check) {
       return check?usefulProxySet.readAll():rawProxySet.readAll();
    }

    /**
     * 更新一个代理
     *
     * @param proxy 代理
     * @param check 代理队列
     * @return 更新结果
     */
    @Override
    public boolean UpdateProxy(Proxy proxy, boolean check) {
        return true;
    }
}
