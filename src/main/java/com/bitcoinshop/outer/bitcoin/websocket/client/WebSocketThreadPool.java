package com.bitcoinshop.outer.bitcoin.websocket.client;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class WebSocketThreadPool {
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
}
