package com.bitcoinshop.outer.bitcoin.websocket.config;

import com.bitcoinshop.outer.bitcoin.websocket.client.WebSocketClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;

@Component
@RequiredArgsConstructor
public class NettySetting {

    public SslContext webSocketSsl() throws SSLException {
        return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
    }

    public Bootstrap bootstrap(String host, int port, WebSocketClientHandler handler) {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws SSLException {
                        ChannelPipeline p = ch.pipeline();

                        p.addLast(webSocketSsl().newHandler(ch.alloc(), host, port));
                        p.addLast(
                                new HttpClientCodec(),
                                new HttpObjectAggregator(8192),
                                WebSocketClientCompressionHandler.INSTANCE,
                                handler);

                    }
                });

        return b;
    }

}
