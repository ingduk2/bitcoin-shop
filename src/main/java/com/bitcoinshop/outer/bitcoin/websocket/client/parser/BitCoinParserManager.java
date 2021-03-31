package com.bitcoinshop.outer.bitcoin.websocket.client.parser;

import com.bitcoinshop.outer.bitcoin.websocket.domain.bithumb.parser.BithumbParser;
import com.bitcoinshop.outer.bitcoin.websocket.domain.korbit.parser.KorBitParser;
import com.bitcoinshop.outer.bitcoin.websocket.domain.upbit.parser.UpbitParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class BitCoinParserManager {
    private final BitCoinParser bithumbParser;
    private final BitCoinParser upbitParser;
    private final BitCoinParser korBitParser;

    private Map<String, BitCoinParser> bithumbParserMap;

    public Map<String, BitCoinParser> getBithumbParserMap() {
        return bithumbParserMap;
    }

    @PostConstruct
    public void init() {
        bithumbParserMap = new ConcurrentHashMap<>();
        bithumbParserMap.put("bithumb", bithumbParser);
        bithumbParserMap.put("upbit", upbitParser);
        bithumbParserMap.put("korbit", korBitParser);
    }
}
