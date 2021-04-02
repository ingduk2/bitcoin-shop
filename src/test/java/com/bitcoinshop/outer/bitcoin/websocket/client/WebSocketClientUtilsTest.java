package com.bitcoinshop.outer.bitcoin.websocket.client;

import com.bitcoinshop.outer.bitcoin.websocket.config.ServerInfo;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketClientUtilsTest {

    @Test
    void 서버정보유틸_테스트() throws URISyntaxException {
        //when
        URI uri = new URI("wss://api.upbit.com/websocket/v1");

        //given
        ServerInfo serverInfo = WebSocketClientUtils.getServerInfo(uri);

        //when
        assertThat(serverInfo.getScheme()).isEqualTo("wss");
        assertThat(serverInfo.getHost()).isEqualTo("api.upbit.com");
        assertThat(serverInfo.getPort()).isEqualTo(443);
    }

    @Test
    void 웹소켓URL스키마_유효성체크_성공() throws URISyntaxException {
        //when
        URI uri = new URI("wss://api.upbit.com/websocket/v1");

        boolean isValid = WebSocketClientUtils.isValidUrlScheme(uri.getScheme());

        assertThat(isValid).isTrue();
    }

    @Test
    void 웹소켓URL스키마_유효성체크_실패() throws URISyntaxException {
        //when
        URI uri = new URI("http://failschema.com");

        boolean isValid = WebSocketClientUtils.isValidUrlScheme(uri.getScheme());

        assertThat(isValid).isFalse();
    }
}