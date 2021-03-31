package com.bitcoinshop.outer.bitcoin.websocket.config;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class WebSocketConnectionDto {
    private final String name;
    private final String url;
}
