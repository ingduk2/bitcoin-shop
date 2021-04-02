package com.bitcoinshop.outer.bitcoin.websocket.client;

import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import com.bitcoinshop.outer.bitcoin.websocket.config.NettySetting;
import com.bitcoinshop.outer.bitcoin.websocket.config.ServerInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class WebSocketClient {

    private final NettySetting setting;
    private final URI uri;
    private final BitCoinParser bitCoinParser;

    private Bootstrap bootstrap;

    public void start() throws Exception {

        log.info("WebSocketClient Start!!!");
        //get serverInfo
        ServerInfo serverInfo = WebSocketClientUtils.getServerInfo(uri);
        final String host = serverInfo.getHost();
        final int port = serverInfo.getPort();

        //validate scheme
        if (!WebSocketClientUtils.isValidUrlScheme(serverInfo.getScheme())) {
            log.error("Only ws(s) supported");
            return;
        }

        //get bootstrap
        bootstrap = setting.bootstrap(
                host
                ,port
                ,new WebSocketClientHandler(uri, bitCoinParser, this)
        );

        log.info("bootstrap {}", bootstrap);

        //connect
        connect(host, port);

    }

    public ChannelFuture connect(String host, int port) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(host, port);

        future.addListener((ChannelFutureListener) future1 -> {
            log.info("addListener");
            if (future1.isSuccess()) {
                log.info("start client success {} {}", host, port);
            } else {
                final EventLoopGroup loop = future1.channel().eventLoop();
                loop.schedule(() -> {
                    try {
                        log.info("reconnection : {} {}", host, port);
                        connect(host, port);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, 2, TimeUnit.SECONDS);
            }
        });
        return future;
    }


}
