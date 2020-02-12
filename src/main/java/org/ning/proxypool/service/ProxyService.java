package org.ning.proxypool.service;

import org.ning.proxypool.domain.Proxy;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author NingWang
 * 2020-02-11 14:29
 */
public interface ProxyService {
    /**
     *保存一个代理
     * @param proxy 代理
     * @param check 是否校验
     * @param filter 是否去重
     */
    void saveProxy(Proxy proxy, boolean check, boolean filter);

    /**
     * 代理总数统计
     * @param check true: 已进行初始校验的队列/ false: 未进行初始校验的队列
     * @return void
     */
     int proxyCount(boolean check);

    /**
     * 去除一个代理
     * @param proxy 代理
     * @param check 是否校验
     */
    void removeProxy(Proxy proxy, boolean check);

    /**
     * 随机获取一个代理
     * @param check 是否校验
     * @return Proxy
     */
    Proxy getProxy(boolean check);

    /**
     * 获取全部代理
     * @param check 是否校验
     * @return 代理集合
     */
    Set<Proxy> getAllProxy(boolean check);

    /**
     * 更新一个代理
     * @param proxy 代理
     * @param check 代理队列
     * @return 更新结果
     */
    boolean UpdateProxy(Proxy proxy, boolean check);

}
