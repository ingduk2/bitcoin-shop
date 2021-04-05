package com.bitcoinshop.outer.bitcoin.websocket.model.korbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

class KorBitSubscribeTest {

    @Test
    void korbit_요청_json_생성_테스트() throws JsonProcessingException {
        String json = KorBitSubscribe.of(null, KorBitSubscribe.Events.SUBSCRIBE, "ticker:btc_krw,eth_krw,xrp_krw");
        System.out.println(json);
    }

}