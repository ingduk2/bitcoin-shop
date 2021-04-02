package com.bitcoinshop.outer.bitcoin.websocket.client.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.Channel;

public interface BitCoinParser {
    void parse(Object frame, Channel channel) throws JsonProcessingException;
}
