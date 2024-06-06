package io.unbong.ubgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * gateway router
 *
 * @author <a href="ecunbong@gmail.com">unbong</a>
 * 2024-05-21 21:32
 */
//@Component
public class GatewayRouter {

//    @Autowired
//    HelloHandler helloHandler;
//
//    @Autowired
//    GateWayHandler gw;
//
//    @Bean
//    public RouterFunction<?> helloRouterFunction(){
//
//        return route(GET("/hello"), helloHandler::handle);
//    }
//
//    @Bean
//    public RouterFunction<?> gatewayRouterFunction(){
//
//        return route(GET("/gw").or(POST("/gw/**")), gw::handler);
//    }



}
