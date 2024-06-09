package io.unbong.ubgateway;

import io.unbong.ubrpc.core.api.RegistryCenter;
import io.unbong.ubrpc.core.registry.ub.UbRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static io.unbong.ubgateway.GatewayPlugin.GATEWAY_PREFIX;

/**
 * gateway config
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-21 22:11
 */
@Configuration
public class GatewayConfig {

//    @Bean
//    public RegistryCe


    /**
     * map url and its handler
     * @param applicationContext
     * @return
     */
    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext applicationContext){
        return x->{
            SimpleUrlHandlerMapping handlerMapping =applicationContext.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put(GATEWAY_PREFIX+"/**", "gateWayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
        };
    }
    @Bean
    public RegistryCenter rc(){
        return new UbRegistryCenter();
    }

}
