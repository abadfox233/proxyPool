package org.ning.proxypool.config;

import lombok.extern.slf4j.Slf4j;
import org.ning.proxypool.service.ProxyScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author NingWang
 * 2020-02-12 13:37
 * Proxy check 定时任务
 */
@Component
@Slf4j
public class ScheduleProxyCheck implements SchedulingConfigurer {

    @Autowired
    ProxyScheduleService proxyScheduleService;

    /**
     * cron表达式
     */
    @Value("${proxy.check.cron}")
    private  String cron;
    /**
     * 任务名称
     */
    private String name="proxy check";

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private Runnable doCheck() {
        return new Runnable() {
            @Override
            public void run() {
                proxyScheduleService.checkProxy();
            }
        };
    }



    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

        scheduledTaskRegistrar.addTriggerTask(doCheck(), getTrigger());

    }

    /**
     * 业务触发器
     * @return trigger
     */
    private Trigger getTrigger() {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 触发器
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        };
    }


}
