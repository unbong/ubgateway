package io.unbong.ubgateway.plugin;

import io.unbong.ubgateway.AbstractGateWayPlugin;
import io.unbong.ubgateway.GatewayPluginChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-08 14:59
 */
@Slf4j
@Component("direct")
public class DirectPlugin extends AbstractGateWayPlugin {

    public static final String NAME = "direct";
    private String prefix =GATEWAY_PREFIX +"/" + NAME +"/";

    /**
     *
     *
     */

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {

        log.debug("---> direct plugin");
        String backend = exchange.getRequest().getQueryParams().getFirst("backend");
        Flux<DataBuffer> requestBody =  exchange.getRequest().getBody();
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("ub.gw.version", "v1.0.01");
        exchange.getResponse().getHeaders().add("ub.gw.plugin", getName());


        if(backend == null || backend.isEmpty())
        {
            return requestBody.flatMap(x->exchange.getResponse().writeWith(Mono.just(x)))
                    .then(chain.handle(exchange));
        }

        // 5 通过webclient 发送post请求
        WebClient client = WebClient.create(backend);

        // 获取相应报文
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody,DataBuffer.class).retrieve().toEntity(String.class);

        Mono<String> body = entity.map(ResponseEntity::getBody);
        // 返回报文

        return body.flatMap(x-> exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(chain.handle(exchange));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
