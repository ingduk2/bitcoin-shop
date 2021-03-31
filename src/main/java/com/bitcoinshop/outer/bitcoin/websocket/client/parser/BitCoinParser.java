package com.bitcoinshop.outer.bitcoin.websocket.client.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface BitCoinParser {
    void parse(WebSocketFrame frame, Channel channel) throws JsonProcessingException;
}
