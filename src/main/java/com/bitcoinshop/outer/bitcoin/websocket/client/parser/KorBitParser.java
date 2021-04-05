package com.bitcoinshop.outer.bitcoin.websocket.client.parser;

import com.bitcoinshop.outer.bitcoin.restapi.service.BitCoinRestApiService;
import com.bitcoinshop.outer.bitcoin.websocket.model.korbit.KorBitSubscribe;
import com.bitcoinshop.outer.bitcoin.websocket.model.korbit.KorbitResponse;
import com.bitcoinshop.web.websocket.config.stomp.StompPushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KorBitParser implements BitCoinParser {

    private static String EVENT_CONNECTED = "korbit:connected";
    private static String EVENT_PUSH_TICKER = "korbit:push-ticker";

    private final ObjectMapper objectMapper;
    private final BitCoinRestApiService restApiService;
    private final StompPushService pushService;


    @Override
    public void parse(Object frame, Channel channel) throws JsonProcessingException {
        if (frame instanceof FullHttpResponse) {
            return;
        }

        if (frame instanceof PongWebSocketFrame) {
        } else if (frame instanceof PingWebSocketFrame) {
            channel.writeAndFlush(new PongWebSocketFrame(Unpooled.buffer(1).writeByte(0xA)));
        } else if (frame instanceof TextWebSocketFrame) {
            responseService((TextWebSocketFrame) frame, channel);
        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("CloseWebSocketFrame");
            channel.close();
        }
    }

    private void responseService(TextWebSocketFrame frame, Channel channel) throws JsonProcessingException {
        TextWebSocketFrame textFrame = frame;
        String recvMsg = textFrame.text();

        JsonNode jsonNode = objectMapper.readTree(recvMsg);
        String event = jsonNode.get("event").asText();

        if (event.equals(EVENT_CONNECTED)) {
            sendSubscribe(channel);

        } else if (event.equals(EVENT_PUSH_TICKER)) {
            KorbitResponse korbitResponse = objectMapper.readValue(recvMsg, KorbitResponse.class);
            log.info("{}", korbitResponse);
            pushService.korbitPush(korbitResponse);
        }
    }

    private void sendSubscribe(Channel channel) throws JsonProcessingException {
        List<String> currencyList = restApiService.getKorbitCurrencyList();
        String ticker = "ticker:" + String.join(",", currencyList);

        String json = KorBitSubscribe.of(null, KorBitSubscribe.Events.SUBSCRIBE, ticker);
        channel.writeAndFlush(new TextWebSocketFrame(json));
    }


}
