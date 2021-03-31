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
        List<WebSocketConnectionDto> webSocketConnectionDtos = webSocketConnectionConfig.getExchanges();

        assertThat(webSocketConnectionDtos.get(0)).usingRecursiveComparison().isEqualTo(new WebSocketConnectionDto("bithumb","wss://pubwss.bithumb.com/pub/ws"));
        assertThat(webSocketConnectionDtos.get(1)).usingRecursiveComparison().isEqualTo(new WebSocketConnectionDto("upbit","wss://api.upbit.com/websocket/v1"));
        assertThat(webSocketConnectionDtos.get(2)).usingRecursiveComparison().isEqualTo(new WebSocketConnectionDto("korbit","wss://ws.korbit.co.kr/v1/user/push"));
    }
}