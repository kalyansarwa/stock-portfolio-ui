package com.kalyansarwa.stockportfolio.model;

import java.time.LocalDateTime;

import yahoofinance.Stock;

public class StockWrapper {
    private final Stock stock;
    private final LocalDateTime lastAccess;

    public StockWrapper(Stock stock) {
        this.stock = stock;
        this.lastAccess = LocalDateTime.now();
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public Stock getStock() {
        return stock;
    }
}
