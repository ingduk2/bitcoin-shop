package com.bitcoinshop.outer.bitcoin.websocket;

import com.bitcoinshop.outer.bitcoin.websocket.client.WebSocketClient;
import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParserManager;
import com.bitcoinshop.outer.bitcoin.websocket.config.WebSocketConnectionConfig;
import com.bitcoinshop.outer.bitcoin.websocket.config.WebSocketConnectionDto;
import com.bitcoinshop.outer.bitcoin.websocket.config.NettySetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketStarter {
    private final NettySetting nettySetting;
    private final ApplicationContext ctx;
    private final WebSocketConnectionConfig webSocketConnectionConfig;
    private final BitCoinParserManager bitCoinParserManager;

    private Map<String, WebSocketClient> webSocketClients = new ConcurrentHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        List<WebSocketConnectionDto> exchanges = webSocketConnectionConfig.getExchanges();

        exchanges.forEach(e -> {
            WebSocketClient bean = null;
            try {
                bean = ctx.getBean(
                        WebSocketClient.class,
                        nettySetting,
                        new URI(e.getUrl()),
                        bitCoinParserManager.getBithumbParserMap().get(e.getName())
                );
                bean.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            webSocketClients.put(e.getName(), bean);
            log.info("webSocketClients {}", webSocketClients);
        });

    }
}
