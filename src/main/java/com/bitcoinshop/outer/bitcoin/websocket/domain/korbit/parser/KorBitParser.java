package com.bitcoinshop.outer.bitcoin.websocket.domain.korbit.parser;

import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import com.bitcoinshop.outer.bitcoin.websocket.domain.korbit.KorBitSubscribeDto;
import com.bitcoinshop.outer.bitcoin.websocket.domain.korbit.dto.KorBitResopnseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class KorBitParser implements BitCoinParser {

    private final ObjectMapper objectMapper;

    @Override
    public void parse(WebSocketFrame frame, Channel channel) throws JsonProcessingException {
        if (frame instanceof PongWebSocketFrame) {

        } else if (frame instanceof PingWebSocketFrame) {
            channel.writeAndFlush(new PongWebSocketFrame(Unpooled.buffer(1).writeByte(0xA)));
        } else if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String recvMsg = textFrame.text();
            log.info("recv Message : {}", recvMsg);

            JsonNode jsonNode = objectMapper.readTree(recvMsg);
            String event = jsonNode.get("event").asText();
            if (event.equals("korbit:connected")) {
                String json = KorBitSubscribeDto.of(null, KorBitSubscribeDto.Events.SUBSCRIBE, Stream.of("ticker:btc_krw,eth_krw,xrp_krw"));
                channel.writeAndFlush(new TextWebSocketFrame(json));
            } else if (event.equals("korbit:push-ticker")) {

            }

        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("CloseWebSocketFrame");
            channel.close();
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            String s = binaryWebSocketFrame.content().toString(StandardCharsets.UTF_8);
        }
    }


}
