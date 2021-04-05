package com.bitcoinshop.outer.bitcoin.websocket.model.bithumb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BithumbSubscribe {
    private BithumbTYPE type;
    private List<String> symbols;
    private List<String> tickTypes;

    public enum BithumbTYPE{
        ticker, transaction, orderbookdepth
    }


    public static String of(BithumbTYPE type, Stream<String> symbols, Stream<String> tickTypes) throws JsonProcessingException {
        BithumbSubscribe bithumbSubscribe = BithumbSubscribe.builder()
                .type(BithumbTYPE.ticker)
                .symbols(symbols.map(String::toUpperCase).collect(Collectors.toList()))
//                .tickTypes(tickTypes.collect(Collectors.toList()))
                    .tickTypes(List.of("30M"))
//                .tickTypes(new ArrayList<>())
                .build();

        return new ObjectMapper().writeValueAsString(bithumbSubscribe);
    }

    public static String getSubscribeJson(List<String> currencyList) throws JsonProcessingException {
        BithumbSubscribe bithumbSubscribe = BithumbSubscribe.builder()
                .type(BithumbTYPE.ticker)
                .symbols(currencyList)
//                .symbols(List.of("BTC_KRW"))
                .tickTypes(List.of("30M"))
                .build();

        return new ObjectMapper().writeValueAsString(bithumbSubscribe);
    }
}
