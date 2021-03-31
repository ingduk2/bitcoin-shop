package com.bitcoinshop.outer.bitcoin.websocket.domain.bithumb.parser;

import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import com.bitcoinshop.outer.bitcoin.websocket.domain.bithumb.BithumbSubscribeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class BithumbParser implements BitCoinParser {

    private final ObjectMapper objectMapper;

    @Override
    public void parse(WebSocketFrame frame, Channel channel) {
        if (frame instanceof PongWebSocketFrame) {
            log.info("PongWebSocketFrame");
        } else if (frame instanceof TextWebSocketFrame) {
            log.info("TextWebSocketFrame");
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            log.info("recv Message : {}", textFrame.text());
        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("CloseWebSocketFrame");
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            String s = binaryWebSocketFrame.content().toString(StandardCharsets.UTF_8);
            log.info("recv Binary : {}",s);
        }
    }

}
