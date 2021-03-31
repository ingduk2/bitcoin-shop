package com.bitcoinshop.outer.bitcoin.websocket;

import com.bitcoinshop.outer.bitcoin.websocket.client.WebSocketClient;
import com.bitcoinshop.outer.bitcoin.websocket.client.WebSocketThreadPool;
import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParserManager;
import com.bitcoinshop.outer.bitcoin.websocket.config.WebSocketConnectionConfig;
import com.bitcoinshop.outer.bitcoin.websocket.config.WebSocketConnectionDto;
import io.netty.handler.ssl.SslContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketStarter {
    private final SslContext webSocketSsl;
    private final ApplicationContext ctx;
    private final WebSocketConnectionConfig webSocketConnectionConfig;
    private final BitCoinParserManager bitCoinParserManager;

    private Map<String, WebSocketClient> webSocketClients = new ConcurrentHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        List<WebSocketConnectionDto> exchanges = webSocketConnectionConfig.getExchanges();

        WebSocketThreadPool webSocketThreadPool = ctx.getBean(WebSocketThreadPool.class);

        exchanges.forEach(e -> {
            WebSocketClient bean = ctx.getBean(
                    WebSocketClient.class,
                    webSocketSsl,
                    webSocketThreadPool,
                    e.getUrl(),
                    bitCoinParserManager.getBithumbParserMap().get(e.getName())
            );


            try {
                bean.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            webSocketClients.put(e.getName(), bean);
        });

    }
}
