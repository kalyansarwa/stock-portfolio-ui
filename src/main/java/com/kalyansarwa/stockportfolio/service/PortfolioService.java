package com.kalyansarwa.stockportfolio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import com.kalyansarwa.stockportfolio.model.StockEntry;
import com.kalyansarwa.stockportfolio.model.StockExit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PortfolioService {

    private static final BigDecimal BROKERAGE_PERCENTAGE = new BigDecimal("0.0027");
    private static final BigDecimal STT_PERCENTAGE = new BigDecimal("0.000995");
    private static final BigDecimal TURNOVERCHARGE_PERCENTAGE = new BigDecimal("0.0000345");
    private static final BigDecimal STAMPDUTY_PERCENTAGE = new BigDecimal("0.00015");
    private static final BigDecimal GST_PERCENTAGE = new BigDecimal("0.18");

    @Autowired
    private StockEntryClientService stockEntryClientService;

    @Autowired
    private StockExitClientService stockExitClientService;

    @Autowired
    private YahooStockService yahooStockService;

    public List<StockExit> findAllStockExits() {
        List<StockExit> allStocks = stockExitClientService.getstockExits();
        return allStocks;
    }

    public List<StockEntry> findAllStockEntries(String filterText) {

        List<StockEntry> allStocks = stockEntryClientService.getStockEntries();
        log.info("stock entries received : {}", allStocks);
        // if (filterText == null || filterText.isEmpty())
        // allStocks = stockRepo.findAll();
        // else
        // allStocks = stockRepo.search(filterText);

        for (StockEntry s : allStocks) {

            s.setCurrentMarketPrice(calculateMarketPrice(s.getSymbol()));

            // s.setPurchaseBrokerage(calculateBrokerage(s.getPurchasePricePerUnit(),
            // s.getQuantity(), true));

            s.setCurrentPrice(calculateCurrentPrice(s.getCurrentMarketPrice(), s.getQuantity()));

            s.setGainOrLoss(calculateGainOrLoss(s.getCurrentPrice(), s.getTotalPurchasePrice()));

            s.setPercentageChange(calculatePercentageChange(s.getCurrentMarketPrice(), s.getPurchasePricePerUnit()));
        }
        return allStocks;
    }

    private BigDecimal roundMe(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePercentageChange(BigDecimal currentMarketPrice, BigDecimal price) {
        Double value = (currentMarketPrice.doubleValue() - price.doubleValue())
                / price.doubleValue() * 100;
        return roundMe(new BigDecimal(value));
    }

    private BigDecimal calculateGainOrLoss(BigDecimal currentPrice, BigDecimal totalPrice) {
        return roundMe(currentPrice.subtract(totalPrice));
    }

    private BigDecimal calculateCurrentPrice(BigDecimal currentMarketPrice, Integer quantity) {

        return roundMe((currentMarketPrice.multiply(BigDecimal.valueOf(quantity)))
                .subtract(calculateBrokerage(currentMarketPrice, quantity, false)));

    }

    private BigDecimal calculateTotalPurchaseCost(BigDecimal price, Integer quantity, BigDecimal puchaseBrokerage) {
        return roundMe((price.multiply(BigDecimal.valueOf(quantity))).add(puchaseBrokerage));
    }

    private BigDecimal calculateTotalSaleCost(BigDecimal price, Integer quantity, BigDecimal saleBrokerage) {
        return roundMe((price.multiply(BigDecimal.valueOf(quantity))).subtract(saleBrokerage));
    }

    private BigDecimal calculateMarketPrice(String symbol) {
        return roundMe(yahooStockService.findStock(symbol + ".NS").getStock().getQuote().getPrice());
    }

    // private BigDecimal calculateMarketCap(String symbol) {
    // return roundMe(yahooStockService.findStock(symbol +
    // ".NS").getStock().getStats().getMarketCap().divide(new
    // BigDecimal(10000000)));
    // }

    private BigDecimal calculateBrokerage(BigDecimal price, Integer quantity, boolean buy) {

        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        BigDecimal brokerage = totalPrice.multiply(BROKERAGE_PERCENTAGE);
        BigDecimal stt = new BigDecimal("0");
        if (buy)
            stt = totalPrice.multiply(STT_PERCENTAGE);
        BigDecimal turnoverCharge = totalPrice.multiply(TURNOVERCHARGE_PERCENTAGE);
        BigDecimal stampDuty = totalPrice.multiply(STAMPDUTY_PERCENTAGE);
        BigDecimal gst = GST_PERCENTAGE.multiply(brokerage.add(turnoverCharge));
        return brokerage.add(stt).add(turnoverCharge).add(stampDuty).add(gst);

    }

    public void save(StockEntry s) {
        if (s == null) {
            System.err.println("StockItem is null");
        }
        s.setPortfolioId("skalyan");
        s.setPurchaseBrokerage(calculateBrokerage(s.getPurchasePricePerUnit(), s.getQuantity(), true));
        s.setTotalPurchasePrice(
                calculateTotalPurchaseCost(s.getPurchasePricePerUnit(), s.getQuantity(), s.getPurchaseBrokerage()));
        s.setHolding(true);
        stockEntryClientService.saveStockEntry(s);

    }

    public int countStocks() {
        return stockEntryClientService.getStockEntriesCount().intValue();
    }

    public void deleteEntry(StockEntry s) {
        stockEntryClientService.deleteStockEntry(s);
    }

    public Collection<String> getStockSymbols() {
        List<StockEntry> allStocks = stockEntryClientService.getStockEntries();
        List<String> stockSymbols = allStocks.stream().map(StockEntry::getSymbol).toList();
        return stockSymbols;
    }

    public void stockExit(String symbol, LocalDate exitDate, Double price) {
        // get the stock entry for this symbol
        StockEntry stockEntry = stockEntryClientService.getStockEntryBySymbol(symbol);
        stockEntry.setCurrentMarketPrice(calculateMarketPrice(stockEntry.getSymbol()));
        stockEntry.setPurchaseBrokerage(
                calculateBrokerage(stockEntry.getPurchasePricePerUnit(), stockEntry.getQuantity(), true));
        stockEntry.setCurrentPrice(calculateCurrentPrice(stockEntry.getCurrentMarketPrice(), stockEntry.getQuantity()));
        stockEntry.setGainOrLoss(calculateGainOrLoss(stockEntry.getCurrentPrice(), stockEntry.getTotalPurchasePrice()));
        stockEntry.setPercentageChange(
                calculatePercentageChange(stockEntry.getCurrentMarketPrice(), stockEntry.getPurchasePricePerUnit()));

        // create a stock exit object
        StockExit stockExit = new StockExit();
        stockExit.setEntryDate(stockEntry.getEntryDate());
        stockExit.setExitDate(exitDate);
        stockExit.setPortfolioId(stockEntry.getPortfolioId());
        stockExit.setPurchaseBrokerage(stockEntry.getPurchaseBrokerage());
        stockExit.setPurchasePricePerUnit(stockEntry.getPurchasePricePerUnit());
        stockExit.setQuantity(stockEntry.getQuantity());
        stockExit.setSalePricePerUnit(new BigDecimal(price));
        stockExit.setSaleBrokerage(calculateBrokerage(stockExit.getSalePricePerUnit(), stockExit.getQuantity(), false));
        stockExit.setSymbol(symbol);
        stockExit.setTotalPurchasePrice(stockEntry.getTotalPurchasePrice());
        stockExit.setTotalSalePrice(calculateTotalSaleCost(stockExit.getSalePricePerUnit(), stockExit.getQuantity(),
                stockExit.getSaleBrokerage()));
        stockExit.setPreTaxGain(stockExit.getTotalSalePrice().subtract(stockExit.getTotalPurchasePrice()));
        // add the stock exit object
        stockExitClientService.saveStockExit(stockExit);
        // delete the stock entry object
        stockEntryClientService.deleteStockEntry(stockEntry);
    }

}
