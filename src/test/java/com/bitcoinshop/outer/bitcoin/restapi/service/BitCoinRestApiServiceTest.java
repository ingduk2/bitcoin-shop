package com.bitcoinshop.outer.bitcoin.restapi.service;

import com.bitcoinshop.outer.bitcoin.restapi.dto.UpbitCurremcyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RestClientTest(BitCoinRestApiService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class BitCoinRestApiServiceTest {
    @Autowired
    private BitCoinRestApiService restApiService;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void restApi_테스트() {
        String url = "https://api.upbit.com/v1/market/all";

        ResponseEntity<List<UpbitCurremcyDto>> result = restApiService.requestGetApi(url, new ParameterizedTypeReference<List<UpbitCurremcyDto>>() {});

        List<UpbitCurremcyDto> body1 = result.getBody();
        for (UpbitCurremcyDto upbitCurremcyDto : body1) {
            System.out.println(upbitCurremcyDto.getMarket());
        }
    }

    @Test
    void getUpbitCurrencyList_테스트() {
        //given
        String json = "[" +
                "{\"market\": \"KRW-BTC\", " +
                "\"korean_name\": \"비트코인\", " +
                "\"english_name\": \"Bitcoin\" }," +
                " { \"market\": \"KRW-ETH\", " +
                "\"korean_name\": \"이더리움\", " +
                "\"english_name\": \"Ethereum\" }" +
                "]";

        mockServer.expect(requestTo("https://api.upbit.com/v1/market/all"))
        .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        //when
        List<UpbitCurremcyDto> upbitCurremcyDtos = restApiService.getUpbitCurrencyList();

        //then
        assertThat(upbitCurremcyDtos.size()).isEqualTo(2);
        assertThat(upbitCurremcyDtos.get(0).getMarket()).isEqualTo("KRW-BTC");
        assertThat(upbitCurremcyDtos.get(1).getMarket()).isEqualTo("KRW-ETH");
    }

    @Test
    void getBithumbCurrencyList_테스트() throws JsonProcessingException {
        //given
        String json = "{ \"status\":\"0000\"," +
                "\"data\":{"+
                "\"BTC\":{\"timestamp\":12313456}," +
                "\"ETH\":{\"timestamp\":12345678}," +
                "\"LTC\":{\"timestamp\":12345678}" +
                "}"+
                "}";

        mockServer.expect(requestTo("https://api.bithumb.com/public/ticker/ALL_KRW"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        //when
        List<String> currencyList = restApiService.getBithumbCurrencyList();
        assertThat(currencyList.size()).isEqualTo(3);
        assertThat(currencyList.get(0)).isEqualTo("BTC_KRW");
        assertThat(currencyList.get(1)).isEqualTo("ETH_KRW");
        assertThat(currencyList.get(2)).isEqualTo("LTC_KRW");
    }

    @Test
    void getKorbitCurrencyList_테스트() throws JsonProcessingException {
        String json = "{" +
                "\"bch_krw\":{\"timestamp\":12313456}," +
                "\"fet_krw\":{\"timestamp\":12345678}," +
                "\"fil_krw\":{\"timestamp\":12345678}" +
                "}";

        mockServer.expect(requestTo("https://api.korbit.co.kr/v1/ticker/detailed/all"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<String> currencyList = restApiService.getKorbitCurrencyList();
        assertThat(currencyList.size()).isEqualTo(3);
        assertThat(currencyList.get(0)).isEqualTo("bch_krw");
        assertThat(currencyList.get(1)).isEqualTo("fet_krw");
        assertThat(currencyList.get(2)).isEqualTo("fil_krw");
    }
}