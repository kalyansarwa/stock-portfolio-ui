package com.kalyansarwa.stockportfolio.service;

import com.kalyansarwa.stockportfolio.model.StockWrapper;

import org.springframework.stereotype.Service;

import yahoofinance.YahooFinance;

@Service
public class YahooStockService {
    public StockWrapper findStock(String symbol) {
        try {
            return new StockWrapper(YahooFinance.get(symbol));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
