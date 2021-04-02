package com.bitcoinshop.outer.bitcoin.websocket.domain.korbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class KorBitSubscribeDtoTest {

    @Test
    void korbit_요청_json_생성_테스트() throws JsonProcessingException {
        String json = KorBitSubscribeDto.of(null, KorBitSubscribeDto.Events.SUBSCRIBE, "ticker:btc_krw,eth_krw,xrp_krw");
        System.out.println(json);
    }

}