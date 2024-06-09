package io.unbong.ubgateway.plugin;

import io.unbong.ubgateway.AbstractGateWayPlugin;
import io.unbong.ubgateway.GatewayPluginChain;
import io.unbong.ubrpc.core.api.LoadBalancer;
import io.unbong.ubrpc.core.api.RegistryCenter;
import io.unbong.ubrpc.core.cluster.RoundRibonLoadBalancer;
import io.unbong.ubrpc.core.meta.InstanceMeta;
import io.unbong.ubrpc.core.meta.ServiceMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-08 14:54
 */
@Slf4j
@Component("ubrpc")
public class UBRpcPlugin extends AbstractGateWayPlugin {

    public static final String NAME = "ubrpc";
    private String prefix =GATEWAY_PREFIX +"/" + NAME +"/";

    /**
     * 用路径来匹配某个插件
     *
     */
    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer=new RoundRibonLoadBalancer<>();

    /**
     *
     * @param exchange
     * @return
     */
    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {

        log.debug("---> ubrpc plutin");

        // 1 通过请求路径或者服务名
        String service = exchange.getRequest().getPath().value()
                .substring(prefix.length());

        ServiceMeta serviceMeta = ServiceMeta.builder()
                .app("app1")
                .env("dev")
                .namespace("public")
                .name(service)
                .build();

        // 2 通过注册中心拿到活着的服务实例
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);


        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);

        String url = instanceMeta.toURL();
        // 3 简化处理 或者得到url
//        String url = instanceMetas.get(0).toURL();

        // 4 拿到请求的报文
        Flux<DataBuffer> requestBody= exchange.getRequest().getBody();

        // 5 通过webclient 发送post请求
        WebClient client = WebClient.create(url);

        // 获取相应报文
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody,DataBuffer.class).retrieve().toEntity(String.class);

        Mono<String> body = entity.map(ResponseEntity::getBody);
        // 返回报文

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("ub.gw.version", "v1.0.01");
        return body.flatMap(x-> exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(chain.handle(exchange));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
