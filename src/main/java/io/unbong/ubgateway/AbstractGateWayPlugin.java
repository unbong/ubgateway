package io.unbong.ubgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-08 14:47
 */
@Slf4j
public abstract class AbstractGateWayPlugin implements  GatewayPlugin {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Mono<Void>  handle(ServerWebExchange exchange, GatewayPluginChain chain) {
        boolean support = doSupport(exchange);
        log.debug("---> plugin[{}], support: {}", this.getName(), support);
        return support? doHandle(exchange, chain): chain.handle(exchange);
    }


    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    public abstract boolean doSupport(ServerWebExchange exchange) ;

    public abstract Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) ;
}
