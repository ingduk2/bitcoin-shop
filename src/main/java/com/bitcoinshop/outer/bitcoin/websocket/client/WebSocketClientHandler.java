package com.bitcoinshop.outer.bitcoin.websocket.client;

import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import com.bitcoinshop.outer.bitcoin.websocket.config.ServerInfo;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final URI uri;
    private final BitCoinParser bitCoinParser;
    private final WebSocketClient webSocketClient;

    private WebSocketClientHandshaker handshaker;

    private ChannelPromise handshakeFuture;

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.debug("handlerAdded");
        handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("channelActive");
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("WebSocket Client channelInactive!!!!!");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                log.debug("reconnect");
                ServerInfo serverInfo = WebSocketClientUtils.getServerInfo(uri);
                final String host = serverInfo.getHost();
                final int port = serverInfo.getPort();
                try {
                    webSocketClient.connect(host, port);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },1, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        log.info("msg {}", msg);
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                handshakeFuture.setSuccess();
            } catch (Exception e) {
                log.error("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }

        }

        bitCoinParser.parse(msg, ch);

    }
}
