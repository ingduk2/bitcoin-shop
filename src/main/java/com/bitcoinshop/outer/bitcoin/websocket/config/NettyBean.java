package com.bitcoinshop.outer.bitcoin.websocket.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;

@Configuration
@RequiredArgsConstructor
public class NettyBean {

    @Bean(name = "webSocketSsl")
    public SslContext webSocketSsl() throws SSLException {
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
    }

//    @Bean(name = "webSocketBootStrap")
//    public Bootstrap bootstrap() {
//
//    }

}
