package com.bitcoinshop.outer.bitcoin.websocket.config;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class WebSocketConnection {
    private final String name;
    private final String url;
}
