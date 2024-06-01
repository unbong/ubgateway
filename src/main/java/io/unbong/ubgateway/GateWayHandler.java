package io.unbong.ubgateway;

import io.unbong.ubrpc.core.api.LoadBalancer;
import io.unbong.ubrpc.core.api.RegistryCenter;
import io.unbong.ubrpc.core.cluster.RoundRibonLoadBalancer;
import io.unbong.ubrpc.core.meta.InstanceMeta;
import io.unbong.ubrpc.core.meta.ServiceMeta;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Description
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-21 21:47
 */

@Component
public class GateWayHandler {

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer=new RoundRibonLoadBalancer<>();
    Mono<ServerResponse> handler(ServerRequest request){

        // 1 通过请求路径或者服务名
        String service = request.path().substring(1).substring(3);

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
        Mono<String> requestJson = request.bodyToMono(String.class);

        return requestJson.flatMap(x->{
            return invokeFromRegistry(url, x);
        });


    }

    @NotNull
    private  Mono<ServerResponse> invokeFromRegistry(String url, String x) {
        // 5 通过webclient 发送post请求
        WebClient client = WebClient.create(url);
//        Mono<ResponseEntity<String>> entity = client.post()
//                .header("Content-Type","application/json")
//                .bodyValue(requestJson).retrieve().toEntity(String.class);

        // 获取相应报文
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(x).retrieve().toEntity(String.class);

        Mono<String> body = entity.map(ResponseEntity::getBody);
        // 返回报文
        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("ub.gw.version", "v1.0.01")
                .body(body, String.class);
    }


}
