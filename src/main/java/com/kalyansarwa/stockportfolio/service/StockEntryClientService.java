package com.kalyansarwa.stockportfolio.service;

import java.util.ArrayList;
import java.util.List;

import com.kalyansarwa.stockportfolio.model.StockEntry;

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
public class StockEntryClientService {

    private static final String URL = "http://localhost:7070/stockEntries";

    @Autowired
    RestTemplate template;

    public List<StockEntry> getStockEntries() {

        var stockEntriesResponse = template.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<StockEntry>>() {
                });

        log.info("stockEntriesResponse = {}", stockEntriesResponse);
        // List<StockEntry> stockEntryList = new ArrayList<>();
        // stockEntriesResponse.getBody().forEach(entry -> stockEntryList.add(entry));

        //return stockEntryList;
        return stockEntriesResponse.getBody();
    }

    public StockEntry getStockEntryBySymbol(String symbol) {

        var stockEntry = template.exchange(URL + "/" + symbol, HttpMethod.GET, null, new ParameterizedTypeReference<StockEntry>() {
            
        });
        return stockEntry.getBody();
    }

    public void saveStockEntry(StockEntry stockEntry) {
        template.exchange(URL, HttpMethod.POST, new HttpEntity<StockEntry>(stockEntry), Void.class);
    }

    public void deleteStockEntry(StockEntry stockEntry) {
        template.exchange(URL + "/" + stockEntry.getSymbol(), HttpMethod.DELETE, null, Void.class);
    }

    public Long getStockEntriesCount() {
        var stockEntriesResponse = template.exchange(URL + "/search/findCount", HttpMethod.GET, null,
                new ParameterizedTypeReference<Long>() {
                });
        log.info("stockEntriesResponse for count : {}", stockEntriesResponse.getBody());
        return stockEntriesResponse.getBody();
    }
}
