package com.kalyansarwa.stockportfolio.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockExit  extends RepresentationModel<StockExit>{

    String id;
    String portfolioId = "skalyan";
    String symbol;
    LocalDate entryDate;
    BigDecimal purchasePricePerUnit;
    Integer quantity;
    BigDecimal purchaseBrokerage;
    BigDecimal totalPurchasePrice;
    LocalDate exitDate;
    BigDecimal salePricePerUnit;
    BigDecimal saleBrokerage;
    BigDecimal totalSalePrice;
    BigDecimal preTaxGain;

}
