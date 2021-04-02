package com.bitcoinshop.outer.bitcoin.websocket.domain.upbit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UpbitResponseDto {
    private String ty; //type
    private String cd; //code
    private String tp; //trade_price
}
