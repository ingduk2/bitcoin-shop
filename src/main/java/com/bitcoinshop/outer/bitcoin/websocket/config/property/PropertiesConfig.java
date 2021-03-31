package com.bitcoinshop.outer.bitcoin.websocket.config.property;

import com.bitcoinshop.outer.bitcoin.websocket.config.WebSocketConnectionConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {WebSocketConnectionConfig.class})
public class PropertiesConfig {
}
