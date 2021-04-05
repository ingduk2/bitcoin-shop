package com.bitcoinshop.outer.bitcoin.websocket.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebSocketConnectionConfigTest {

    @Autowired
    WebSocketConnectionConfig webSocketConnectionConfig;

    @Test
    void 거래소url_설정파일_로딩_테스트() {
        List<WebSocketConnection> webSocketConnections = webSocketConnectionConfig.getExchanges();

        assertThat(webSocketConnections.get(0)).usingRecursiveComparison().isEqualTo(new WebSocketConnection("bithumb","wss://pubwss.bithumb.com/pub/ws"));
        assertThat(webSocketConnections.get(1)).usingRecursiveComparison().isEqualTo(new WebSocketConnection("upbit","wss://api.upbit.com/websocket/v1"));
        assertThat(webSocketConnections.get(2)).usingRecursiveComparison().isEqualTo(new WebSocketConnection("korbit","wss://ws.korbit.co.kr/v1/user/push"));
    }
}