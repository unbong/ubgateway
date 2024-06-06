package io.unbong.ubgateway;

import io.unbong.ubrpc.core.api.LoadBalancer;
import io.unbong.ubrpc.core.api.RegistryCenter;
import io.unbong.ubrpc.core.cluster.RoundRibonLoadBalancer;
import io.unbong.ubrpc.core.meta.InstanceMeta;
import io.unbong.ubrpc.core.meta.ServiceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * gateway web handler
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-06-01 16:21
 */
@Component("gateWayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer=new RoundRibonLoadBalancer<>();
    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {

        // 1 通过请求路径或者服务名
        String service = exchange.getRequest().getPath().value()
                .substring(1).substring(3);

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
//        Mono<ResponseEntity<String>> entity = client.post()
//                .header("Content-Type","application/json")
//                .bodyValue(requestJson).retrieve().toEntity(String.class);

        // 获取相应报文
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody,DataBuffer.class).retrieve().toEntity(String.class);

        Mono<String> body = entity.map(ResponseEntity::getBody);
        // 返回报文

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("ub.gw.version", "v1.0.01");
        return body.flatMap(x-> exchange.getResponse().writeWith(
           Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))
        ));
        //return Mono.empty();
    }
}
