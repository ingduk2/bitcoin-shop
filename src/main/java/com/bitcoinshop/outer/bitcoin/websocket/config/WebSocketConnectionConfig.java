package com.bitcoinshop.outer.bitcoin.websocket.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "bitcoin.websocket")
public class WebSocketConnectionConfig {
    private final List<WebSocketConnection> exchanges;
}
