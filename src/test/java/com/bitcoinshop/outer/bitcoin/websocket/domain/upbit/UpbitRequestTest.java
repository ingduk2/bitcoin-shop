package com.bitcoinshop.outer.bitcoin.websocket.domain.upbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UpbitRequestTest {

    @Test
    void upbit_요청_json생성_테스트() throws JsonProcessingException {

        String json = UpbitSubscribeDto.of("test", "ticker", Stream.of("KRW-BTC", "BTC-BCH"), "SIMPLE");
        System.out.println(json);

        assertThat(json).isEqualTo(
                "[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\",\"BTC-BCH\"]},{\"format\":\"SIMPLE\"}]");
    }
}