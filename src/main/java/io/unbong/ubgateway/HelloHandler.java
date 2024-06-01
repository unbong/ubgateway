package io.unbong.ubgateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * hello handler.
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-21 21:43
 */
@Component
public class HelloHandler {

    Mono<ServerResponse> handle(ServerRequest request){

        String url = "http://localhost:8081/ubrpc";

        String requestJson = """
                {
                   "service":"io.unbong.ubrpc.demo.api.UserService",
                   "methodSign":"findById@1_int",
                   "args":[100]
                }
                """;
        WebClient client = WebClient.create(url);
//        Mono<ResponseEntity<String>> entity = client.post()
//                .header("Content-Type","application/json")
//                .bodyValue(requestJson).retrieve().toEntity(String.class);

        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestJson).retrieve().toEntity(String.class);


        return  ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("ub.gw.version", "v1.0.01")
                .body(entity.map(ResponseEntity::getBody),String.class);
    }
}
