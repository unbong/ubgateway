package io.unbong.ubgateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-09 15:13
 */
public interface GatewayFilter {
    Mono<Void> filter(ServerWebExchange exchange);

}
