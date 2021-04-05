package com.bitcoinshop.outer.bitcoin.websocket.model.bithumb;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BithumbSubscribeTest {

    @Test
    void bithumb_요청_json_생성_테스트() throws JsonProcessingException {
        String json = BithumbSubscribe.of(BithumbSubscribe.BithumbTYPE.ticker, Stream.of("BTC-KRW"), Stream.of("30M", "1H", "12H", "24H","MID"));

        assertThat(json).isEqualTo(
                "{\"type\":\"ticker\",\"symbols\":[\"BTC_KRW\"],\"tickTypes\":[\"30M\",\"1H\",\"12H\",\"24H\",\"MID\"]}");
    }

}