package com.bitcoinshop.outer.bitcoin.websocket.domain.upbit.parser;

import com.bitcoinshop.outer.bitcoin.restapi.dto.UpbitCurremcyDto;
import com.bitcoinshop.outer.bitcoin.restapi.service.BitCoinRestApiService;
import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import com.bitcoinshop.outer.bitcoin.websocket.domain.upbit.UpbitSubscribeDto;
import com.bitcoinshop.outer.bitcoin.websocket.domain.upbit.dto.UpbitResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpbitParser implements BitCoinParser {
    private final BitCoinRestApiService restApiService;
    private final ObjectMapper objectMapper;

    @Override
    public void parse(Object frame, Channel channel) throws JsonProcessingException {
        if (frame instanceof FullHttpResponse) {
            sendSubscribe(channel);
            return;
        }

        if (frame instanceof PongWebSocketFrame) {

        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("CloseWebSocketFrame");
            channel.close();

        } else if (frame instanceof BinaryWebSocketFrame) {
            responseService((BinaryWebSocketFrame) frame);

        }
    }

    private void responseService(BinaryWebSocketFrame frame) throws JsonProcessingException {
        BinaryWebSocketFrame binaryWebSocketFrame = frame;
        String responseJson = binaryWebSocketFrame.content().toString(StandardCharsets.UTF_8);

        UpbitResponseDto upbitResponseDto = objectMapper.readValue(responseJson, UpbitResponseDto.class);
        log.info("reponse : {}", upbitResponseDto);
    }

    private void sendSubscribe(Channel channel) throws JsonProcessingException {
        List<UpbitCurremcyDto> upbitMarketInfos = restApiService.getUpbitCurrencyList();
        List<String> markets = upbitMarketInfos.stream()
                .map(UpbitCurremcyDto::getMarket).filter(m -> m.contains("KRW"))
                .collect(Collectors.toList());


        String json = UpbitSubscribeDto.getSubscribeJson("test", "ticker", markets, "SIMPLE");
        log.info("sendSubscribe {}", json);
        channel.writeAndFlush(new TextWebSocketFrame(json));
    }


}
