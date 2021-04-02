package com.bitcoinshop.outer.bitcoin.websocket.domain.bithumb.parser;

import com.bitcoinshop.outer.bitcoin.restapi.service.BitCoinRestApiService;
import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import com.bitcoinshop.outer.bitcoin.websocket.domain.bithumb.BithumbSubscribeDto;
import com.bitcoinshop.outer.bitcoin.websocket.domain.bithumb.dto.BithumbResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class BithumbParser implements BitCoinParser {

    private static String STATUS_OK = "0000";
    private static String STATUS_FAIL = "5100";
    private static String CONNECT_SUCCESS = "Connected Successfully";
    private static String FILTER_REGUSTER_SUCCESS = "Filter Registered Successfully";

    private final BitCoinRestApiService restApiService;
    private final ObjectMapper objectMapper;

    @Override
    public void parse(Object frame, Channel channel) throws JsonProcessingException {
        if (frame instanceof FullHttpResponse) {
            return;
        }

        if (frame instanceof PongWebSocketFrame) {
            log.info("PongWebSocketFrame");
        } else if (frame instanceof TextWebSocketFrame) {
            responseService((TextWebSocketFrame) frame, channel);
        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("CloseWebSocketFrame");
            channel.close();
        }
    }

    private void responseService(TextWebSocketFrame frame, Channel channel) throws JsonProcessingException {
        TextWebSocketFrame textFrame = frame;
        String json = textFrame.text();

        JsonNode jsonNode = objectMapper.readTree(json);
        if (jsonNode.has("status")) {
            String status = jsonNode.get("status").asText();
            String resmsg = jsonNode.get("resmsg").asText();

            if (status.equalsIgnoreCase(STATUS_OK) && resmsg.equalsIgnoreCase(CONNECT_SUCCESS)) {
                sendSubscribe(channel);
            }

        } else {
            BithumbResponseDto bithumbResponseDto = objectMapper.readValue(json, BithumbResponseDto.class);
            log.info("{}", bithumbResponseDto);

        }
    }

    private void sendSubscribe(Channel channel) throws JsonProcessingException {
      List<String> currencyList = restApiService.getBithumbCurrencyList();

        String subscribeJson = BithumbSubscribeDto.getSubscribeJson(currencyList);
        log.info("sendSubscribe {}", subscribeJson);
        channel.writeAndFlush(new TextWebSocketFrame(subscribeJson));
    }


}
