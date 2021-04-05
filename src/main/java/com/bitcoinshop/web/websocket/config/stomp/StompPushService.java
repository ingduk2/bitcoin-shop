package com.bitcoinshop.web.websocket.config.stomp;

import com.bitcoinshop.outer.bitcoin.websocket.model.bithumb.BithumbResponse;
import com.bitcoinshop.outer.bitcoin.websocket.model.korbit.KorbitResponse;
import com.bitcoinshop.outer.bitcoin.websocket.model.upbit.UpbitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StompPushService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void upbitPush(UpbitResponse upbitResponse) {
        simpMessagingTemplate.convertAndSend("/topic/upbit", upbitResponse);
    }

    public void korbitPush(KorbitResponse korbitResponse) {
        simpMessagingTemplate.convertAndSend("/topic/korbit", korbitResponse);
    }

    public void bithumbPush(BithumbResponse bithumbResponse) {
        simpMessagingTemplate.convertAndSend("/topic/bithumb", bithumbResponse);
    }
}
