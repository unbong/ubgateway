package io.unbong.ubgateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway plugin chain.
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-08 16:57
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);


}
