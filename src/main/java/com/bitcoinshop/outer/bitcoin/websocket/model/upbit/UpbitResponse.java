package com.bitcoinshop.outer.bitcoin.websocket.model.upbit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UpbitResponse {
    private String ty; //type
    private String cd; //code
    private String tp; //trade_price
}
