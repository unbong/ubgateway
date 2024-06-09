package io.unbong.ubgateway.filter;

import io.unbong.ubgateway.GatewayFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-09 15:07
 */
public class DemoFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {

        System.out.println(" ===>>> filters: demo filter ...");
        exchange.getRequest().getHeaders().toSingleValueMap()
                .forEach((k, v) -> System.out.println(k + ":" + v));
        return Mono.empty();
    }
}
