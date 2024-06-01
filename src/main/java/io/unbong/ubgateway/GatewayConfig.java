package io.unbong.ubgateway;

import io.unbong.ubrpc.core.api.RegistryCenter;
import io.unbong.ubrpc.core.registry.ub.UbRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public RegistryCenter rc(){
        return new UbRegistryCenter();
    }

}
