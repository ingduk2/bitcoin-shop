package com.bitcoinshop.outer.bitcoin.websocket.model.bithumb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class BithumbResponse {

    private String type;
    private Content content;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Content{
        private String closePrice;
        private String symbol;
    }
}
