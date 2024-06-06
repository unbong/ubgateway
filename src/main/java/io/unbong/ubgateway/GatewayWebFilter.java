package io.unbong.ubgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-02 16:12
 */
@Slf4j
@Component
public class GatewayWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.debug("---> gateway web filter");
        if(exchange.getRequest().getQueryParams().getFirst("mock")==null)
        {
            // filter 可以被order
            return chain.filter(exchange);
        }

        String mock = """
                
                    {"result": "mock"}
                """;
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(
                        mock.getBytes()
                )));
    }
}
