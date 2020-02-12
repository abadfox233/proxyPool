package org.ning.proxypool.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author NingWang
 * 2020-02-11 22:13
 */
public interface ProxyScheduleService {
    /**
     * schedule getProxy
     */
    void getProxy();


    void checkProxy();
}
