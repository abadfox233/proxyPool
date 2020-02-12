package org.ning.proxypool.service;

import org.ning.proxypool.domain.Proxy;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;

/**
 * @author NingWang
 * 2020-02-11 18:02
 */

public interface ProxyCheckService {

    /**
     * 代理校验
     * @param check 是否已校验
     */
    @Async
    void checkProxy(Set<Proxy> proxySet, boolean check);

}
