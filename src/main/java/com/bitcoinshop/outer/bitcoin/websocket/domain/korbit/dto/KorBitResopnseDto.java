package com.bitcoinshop.outer.bitcoin.websocket.domain.korbit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
//@ToString
/**
 * connect response
 * "{"timestamp":1617163918623,"key":0,"event":"korbit:connected","data":{}}"
 */
public class KorBitResopnseDto {
    private String accessToken;
    private String event;
    private long timestamp;
    private Object data;
}
