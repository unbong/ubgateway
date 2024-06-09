package io.unbong.ubgateway.web.handler;


import io.unbong.ubgateway.DefaultGatewayPluginChain;
import io.unbong.ubgateway.GatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * gateway web handler
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-01 16:21
 */
@Slf4j
@Component("gateWayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {

        log.debug("---> ub gateway web handler");
        if(plugins == null || plugins.isEmpty())
        {
            String mock = """
                       {"result": "no plugin"}
                    """;
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }

        return new DefaultGatewayPluginChain(plugins).handle(exchange);

    }
}
