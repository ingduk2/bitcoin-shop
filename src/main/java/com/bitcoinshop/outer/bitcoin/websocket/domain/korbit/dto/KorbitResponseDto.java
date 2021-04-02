package com.bitcoinshop.outer.bitcoin.websocket.domain.korbit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class KorbitResponseDto {

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
