package com.bitcoinshop.outer.bitcoin.websocket.domain.bithumb;

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
public class BithumbSubscribeDto {
    private BithumbTYPE type;
    private List<String> symbols;
    private List<String> tickTypes;

    public enum BithumbTYPE{
        ticker, transaction, orderbookdepth
    }


    public static String of(BithumbTYPE type, Stream<String> symbols, Stream<String> tickTypes) throws JsonProcessingException {
        BithumbSubscribeDto bithumbSubscribeDto = BithumbSubscribeDto.builder()
                .type(BithumbTYPE.ticker)
                .symbols(symbols.map(String::toUpperCase).collect(Collectors.toList()))
//                .tickTypes(tickTypes.collect(Collectors.toList()))
                    .tickTypes(List.of("30M"))
//                .tickTypes(new ArrayList<>())
                .build();

        return new ObjectMapper().writeValueAsString(bithumbSubscribeDto);
    }
}
