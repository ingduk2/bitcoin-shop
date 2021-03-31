package com.bitcoinshop.outer.bitcoin.websocket.client;

import com.bitcoinshop.outer.bitcoin.websocket.client.parser.BitCoinParser;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Map;
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
        log.info("handlerAdded");
        handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("channelActive");
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("WebSocket Client channelInactive!!!!!");
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("reconnect");
                Map<String, String> serverInfo = WebSocketClientUtils.getServerInfo(uri);
                final String host = serverInfo.get("host");
                final int port = Integer.parseInt(serverInfo.get("port"));

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
        log.info("handshaker : {}", handshaker.isHandshakeComplete());
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                handshakeFuture.setSuccess();
            } catch (Exception e) {
                log.error("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        bitCoinParser.parse((WebSocketFrame) msg, ch);

    }
}
