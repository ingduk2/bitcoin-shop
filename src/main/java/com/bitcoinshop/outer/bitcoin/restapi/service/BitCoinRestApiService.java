package com.bitcoinshop.outer.bitcoin.restapi.service;

import com.bitcoinshop.outer.bitcoin.restapi.dto.UpbitCurremcyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BitCoinRestApiService<T> {
    private final RestTemplate template;

    public <T> ResponseEntity<T> requestGetApi(String url, ParameterizedTypeReference<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return template.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType);
    }

    public List<UpbitCurremcyDto> getUpbitCurrencyList() {
        String url = "https://api.upbit.com/v1/market/all";
        ResponseEntity<List<UpbitCurremcyDto>> result = requestGetApi(url, new ParameterizedTypeReference<List<UpbitCurremcyDto>>() {});
        return result.getBody();
    }

    public List<String> getBithumbCurrencyList() throws JsonProcessingException {
        String url = "https://api.bithumb.com/public/ticker/ALL_KRW";
        String json = template.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode dataNode = jsonNode.get("data");
        Iterator<String> fields = dataNode.fieldNames();

        List<String> currencyList = new ArrayList<>();
        fields.forEachRemaining(e ->{
            currencyList.add(e+"_KRW");
        });

        return currencyList;
    }

    public List<String> getKorbitCurrencyList() throws JsonProcessingException {
        String url = "https://api.korbit.co.kr/v1/ticker/detailed/all";
        String json = template.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Iterator<String> fields = jsonNode.fieldNames();

        List<String> currencyList = new ArrayList<>();
        fields.forEachRemaining(e ->{
            currencyList.add(e);
        });

        return currencyList;
    }

}
