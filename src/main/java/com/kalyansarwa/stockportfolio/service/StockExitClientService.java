package com.kalyansarwa.stockportfolio.service;

import java.util.ArrayList;
import java.util.List;

import com.kalyansarwa.stockportfolio.model.StockExit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockExitClientService {
    private static final String URL = "http://localhost:8080/stockexits";

    @Autowired
    RestTemplate template;

    public List<StockExit> getstockExits() {

        var stockExitsResponse = template.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<CollectionModel<StockExit>>() {
                });

        log.info("stockExitsResponse = {}", stockExitsResponse);
        List<StockExit> StockExitList = new ArrayList<>();
        stockExitsResponse.getBody().forEach(entry -> StockExitList.add(entry));

        return StockExitList;
    }

    public void saveStockExit(StockExit StockExit) {
        template.exchange(URL, HttpMethod.POST, new HttpEntity<StockExit>(StockExit), Void.class);
    }

    public void deleteStockExit(StockExit StockExit) {
        template.exchange(URL + "/" + StockExit.getSymbol(), HttpMethod.DELETE, null, Void.class);
    }

    public Long getStockExitssCount() {
        var stockExitsResponse = template.exchange(URL + "/search/findCount", HttpMethod.GET, null,
                new ParameterizedTypeReference<Long>() {
                });
        log.info("stockExitsResponse for count : {}", stockExitsResponse.getBody());
        return stockExitsResponse.getBody();
    }
}
