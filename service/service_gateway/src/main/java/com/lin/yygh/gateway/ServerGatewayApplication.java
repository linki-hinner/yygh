package com.lin.yygh.gateway;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig
public class ServerGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class, args);
    }
}
