package com.bitcoinshop.outer.bitcoin.websocket.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ServerInfo {
    private String scheme;
    private String host;
    private int port;
}
