package com.kalyansarwa.stockportfolio.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class StockItem {
    @Id
    @GeneratedValue
    Long id;

    LocalDate entryDate;
    String symbol;
    BigDecimal price;
    Integer quantity;

    BigDecimal marketCap;
    BigDecimal purchaseCost;
    BigDecimal totalPrice;
    BigDecimal currentMarketPrice;
    BigDecimal percentageChange;

    BigDecimal currentPrice;
    BigDecimal gainOrLoss;

}
