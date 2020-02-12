package org.ning.proxypool;

import org.ning.proxypool.plugincore.PluginRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Import(PluginRegister.class)
@EnableScheduling
public class ProxypoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxypoolApplication.class, args);
    }

}
