package io.unbong.ubgateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-08 17:01
 */
public class DefaultGatewayPluginChain implements  GatewayPluginChain{

    List<GatewayPlugin> gatewayPlugins;
    int index= 0;

    public DefaultGatewayPluginChain(List<GatewayPlugin> gatewayPlugins) {
        this.gatewayPlugins = gatewayPlugins;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        // 惰性
        return Mono.defer(()->{
            if(index < gatewayPlugins.size())
            {
                return gatewayPlugins.get(index++).handle(exchange, this);
            }

            return Mono.empty();
        });
    }
}
