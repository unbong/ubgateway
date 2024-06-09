package io.unbong.ubgateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-08 14:43
 */
public interface GatewayPlugin {

    String GATEWAY_PREFIX = "/gw";

    void start();
    void stop();

    String getName();
    boolean support(ServerWebExchange exchange);
    Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain);
}
