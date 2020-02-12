package org.ning.proxypool.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author NingWang
 * 2020-02-11 12:36
 */
@Configuration
public class RedissionBean {


    @Autowired
    private RedisProperties redisProperties;

    // Redission Bean
    @Bean
    public RedissonClient proxyRedissonClient (){
        Config config = new Config();

        String redisUrl = String.format("redis://%s:%s", redisProperties.getHost()+"",redisProperties.getPort()+"");
        config.useSingleServer().setAddress(redisUrl);
        config.useSingleServer().setDatabase(redisProperties.getDatabase());
        config.useSingleServer().setIdleConnectionTimeout(10000);
        config.useSingleServer().setPingTimeout(1000);
        config.useSingleServer().setConnectTimeout(1000);
        config.useSingleServer().setTimeout(3000);
        config.useSingleServer().setRetryAttempts(3);
        config.useSingleServer().setRetryInterval(1500);
        config.useSingleServer().setSubscriptionsPerConnection(5);
        config.useSingleServer().setClientName("none");
        config.useSingleServer().setSubscriptionConnectionMinimumIdleSize(1);
        config.useSingleServer().setSubscriptionConnectionPoolSize(50);
        config.useSingleServer().setConnectionMinimumIdleSize(10);
        config.useSingleServer().setConnectionPoolSize(64);
        config.useSingleServer().setDnsMonitoring(false);
        config.useSingleServer().setDnsMonitoringInterval(5000);
        return Redisson.create(config);

    }

}


