package com.bitcoinshop.outer.bitcoin.websocket.domain.upbit;

import com.fasterxml.classmate.AnnotationOverrides;
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
public class UpbitSubscribeDto {

    private Ticket ticket;
    private Type type;
    private Format format;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Ticket {
        private String ticket;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Type {
        private String type;
        private List<String> codes;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Format {
        private String format = "SIMPLE";
    }

    public static String of(String ticket, String type, Stream<String> codes, String format) throws JsonProcessingException {
        List<Object> objectList = List.of(
                Ticket.builder()
                        .ticket(ticket)
                        .build(),
                Type.builder()
                        .type(type)
                        .codes(codes.map(String::toUpperCase).collect(Collectors.toList()))
                        .build(),
                Format.builder()
                        .format(format)
                        .build()
        );
        return new ObjectMapper().writeValueAsString(objectList);
    }

    public static String getSubscribeJson(String ticket, String type, List<String> markets, String format) throws JsonProcessingException {

        List<Object> objectList = List.of(
                Ticket.builder()
                        .ticket(ticket)
                        .build(),
                Type.builder()
                        .type(type)
                        .codes(List.of("KRW-BTC"))
                        .build(),
                Format.builder()
                        .format(format)
                        .build()
        );
        return new ObjectMapper().writeValueAsString(objectList);
    }
}
