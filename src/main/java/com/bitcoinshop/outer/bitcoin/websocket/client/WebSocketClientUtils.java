package com.bitcoinshop.outer.bitcoin.websocket.client;

import com.bitcoinshop.outer.bitcoin.websocket.config.ServerInfo;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.util.Map;

@UtilityClass
public class WebSocketClientUtils {

    public static ServerInfo getServerInfo(URI uri) {
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        return ServerInfo.builder()
                .scheme(scheme)
                .host(host)
                .port(port)
                .build();
    }

    public static boolean isValidUrlScheme(String scheme) {
        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            return false;
        }
        return true;
    }

}
