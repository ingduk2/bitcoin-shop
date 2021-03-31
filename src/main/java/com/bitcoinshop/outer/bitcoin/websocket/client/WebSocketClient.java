package com.bitcoinshop.outer.bitcoin.websocket.client;

import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class WebSocketClient {

    private final SslContext webSocketSsl;

    private final WebSocketThreadPool webSocketThreadPool;
    private final String URL;
    private final BitCoinParser bitCoinParser;


    private WebSocketClientHandler handler;
    private Bootstrap b;
    private EventLoopGroup group;

    public void start() throws Exception {

        log.info("WebSocketClient Start!!!");
        URI uri = new URI(URL);

        //Map.of("scheme", scheme, "host", host, "port", Integer.toString(port));
        Map<String, String> serverInfo = WebSocketClientUtils.getServerInfo(uri);
        String scheme = serverInfo.get("scheme");
        final String host = serverInfo.get("host");
        final int port = Integer.parseInt(serverInfo.get("port"));
        log.info("ServerInfo :{},", serverInfo);

        //validate scheme
        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            log.error("Only WS(S) is supported");
            return;
        }

//        //ssl
//        final SslContext sslContext = null;
//        if ("wss".equalsIgnoreCase(scheme)) {
//            sslContext = SslContextBuilder.forClient()
//                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
//        } else {
//            sslContext = null;
//        }

        handler = new WebSocketClientHandler(uri, bitCoinParser, this);

        group = new NioEventLoopGroup();

        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        log.info("webSocket initchannel");
                        ChannelPipeline p = ch.pipeline();

                        p.addLast(webSocketSsl.newHandler(ch.alloc(), host, port));
                        p.addLast(
                                new HttpClientCodec(),
                                new HttpObjectAggregator(8192),
                                WebSocketClientCompressionHandler.INSTANCE,
                                handler);

                    }
                });

        connect(uri.getHost(), port);

    }

    public ChannelFuture connect(String host, int port) throws InterruptedException {
        ChannelFuture future = b.connect(host, port);

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
