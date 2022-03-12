package com.kalyansarwa.stockportfolio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.kalyansarwa.stockportfolio.model.StockItem;
import com.kalyansarwa.stockportfolio.repo.StockRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private static final BigDecimal BROKERAGE_PERCENTAGE = new BigDecimal("0.0027");
    private static final BigDecimal STT_PERCENTAGE = new BigDecimal("0.000995");
    private static final BigDecimal TURNOVERCHARGE_PERCENTAGE = new BigDecimal("0.0000345");
    private static final BigDecimal STAMPDUTY_PERCENTAGE = new BigDecimal("0.00015");
    private static final BigDecimal GST_PERCENTAGE = new BigDecimal("0.18");

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private YahooStockService yahooStockService;

    public List<StockItem> findAll(String filterText) {

        List<StockItem> allStocks;
        if (filterText == null || filterText.isEmpty())
            allStocks = stockRepo.findAll();
        else
            allStocks = stockRepo.search(filterText);

        for (StockItem s : allStocks) {

            s.setCurrentMarketPrice(calculateMarketPrice(s.getSymbol()));

            s.setPurchaseCost(roundMe(calculatePurchaseCost(s.getPrice(), s.getQuantity(), true)));

            s.setTotalPrice(calculateTotalCost(s.getPrice(), s.getQuantity(), s.getPurchaseCost()));

            s.setCurrentPrice(calculateCurrentPrice(s.getCurrentMarketPrice(), s.getQuantity(), s.getPrice()));

            s.setGainOrLoss(calculateGainOrLoss(s.getCurrentPrice(), s.getTotalPrice()));

            s.setPercentageChange(calculatePercentageChange(s.getCurrentMarketPrice(), s.getPrice()));

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

    private BigDecimal calculateCurrentPrice(BigDecimal currentMarketPrice, Integer quantity, BigDecimal price) {

        return roundMe((currentMarketPrice.multiply(BigDecimal.valueOf(quantity)))
                .subtract(calculatePurchaseCost(price, quantity, false)));

    }

    private BigDecimal calculateTotalCost(BigDecimal price, Integer quantity, BigDecimal purchaseCost) {
        return roundMe((price.multiply(BigDecimal.valueOf(quantity))).add(purchaseCost));
    }

    private BigDecimal calculateMarketPrice(String symbol) {
        return roundMe(yahooStockService.findStock(symbol + ".NS").getStock().getQuote().getPrice());
    }

    private BigDecimal calculatePurchaseCost(BigDecimal price, Integer quantity, boolean buy) {

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

    public void save(StockItem s) {
        if (s == null) {
            System.err.println("StockItem is null");
        }
        stockRepo.save(s);
    }

    public long countStocks() {
        return stockRepo.count();
    }

    public void deleteEntry(StockItem s) {
        stockRepo.delete(s);
    }

    @PostConstruct
    public void initialize() {
        stockRepo.saveAll(
                Stream.of("01-Mar-2021:ABB:1546.94:9",
                        "30-Apr-2021:JSLHISAR:164.50:100",
                        "13-Aug-2021:VIPIND:456.00:40",
                        "20-Aug-2021:LINDEINDIA:1948.30:9",
                        "27-Sep-2021:GNFC:421.50:42",
                        "15-Nov-2021:THERMAX:1596.25:11",
                        "15-Nov-2021:ATGL:1652.00:10",
                        "29-Nov-2021:VEDL:353.00:50",
                        "13-Dec-2021:ADANIENT:1757.40:10",
                        "20-Dec-2021:POWERGRID:210.55:80",
                        "24-Jan-2022:SHARDACROP:454.30:38",
                        "31-Jan-2022:GAEL:214.95:80",
                        "07-Feb-2022:BANKBARODA:109.05:160",
                        "07-Feb-2022:CGCL:615.00:28",
                        "07-Feb-2022:HINDALCO:530.05:33")
                        .map(entry -> {
                            String[] split = entry.split(":");
                            StockItem stockEntry = new StockItem();
                            stockEntry.setEntryDate(
                                    LocalDate.parse(split[0], DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
                            stockEntry.setSymbol(split[1]);
                            stockEntry.setQuantity(Integer.parseInt(split[3]));
                            stockEntry.setPrice(new BigDecimal(split[2]));
                            return stockEntry;
                        }).collect(Collectors.toList()));
    }
}
