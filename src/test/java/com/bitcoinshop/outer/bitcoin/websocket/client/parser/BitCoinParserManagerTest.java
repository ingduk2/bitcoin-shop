package com.bitcoinshop.outer.bitcoin.websocket.client.parser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BitCoinParserManagerTest {

    @Autowired
    BitCoinParserManager bitCoinParserManager;

    @Test
    void parse매니저_테스트() {
        Map<String, BitCoinParser> parserMap = bitCoinParserManager.getBithumbParserMap();

        assertThat(parserMap.size()).isEqualTo(3);
    }
}