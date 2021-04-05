package com.bitcoinshop.outer.bitcoin.websocket.model.korbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KorBitSubscribe {
    private String accessToken;
    private long timestamp;
    private String event;
    private Channels data;

    public enum Events {
        SUBSCRIBE("korbit:subscribe"),
        UNSUBSCRIBE("korbit:unsubscribe");

        private final String event;
        Events(String event) {
            this.event = event;
        }

        public String getEvent() {
            return event;
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Channels {
        private List<String> channels;
    }

    public static String of(String accessToken, Events event, String channels) throws JsonProcessingException {
        KorBitSubscribe korBitSubscribe = KorBitSubscribe.builder()
                .accessToken(null)
                .timestamp(Instant.now().getEpochSecond())
                .event(event.getEvent())
                .data(Channels.builder()
                        .channels(Stream.of(channels).collect(Collectors.toList()))
                        .build())
                .build();

        return new ObjectMapper().writeValueAsString(korBitSubscribe);
    }
}
