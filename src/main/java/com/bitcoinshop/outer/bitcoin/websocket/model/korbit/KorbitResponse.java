package com.bitcoinshop.outer.bitcoin.websocket.model.korbit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class KorbitResponse {

    private Data data;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Data{
        private String channel;
        private String currency_pair;
        private String last;
    }

}
