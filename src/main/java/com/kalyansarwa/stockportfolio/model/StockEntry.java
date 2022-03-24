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
public class StockEntry extends RepresentationModel<StockEntry> {

    String id;
    String portfolioId = "skalyan";
    String symbol;
    LocalDate entryDate;
    BigDecimal purchasePricePerUnit;
    Integer quantity;
    BigDecimal purchaseBrokerage;
    BigDecimal totalPurchasePrice;

    Boolean holding = true;
    BigDecimal currentMarketPrice;
    BigDecimal percentageChange;
    BigDecimal currentPrice;
    BigDecimal gainOrLoss;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        StockEntry other = (StockEntry) obj;
        if (currentMarketPrice == null) {
            if (other.currentMarketPrice != null)
                return false;
        } else if (!currentMarketPrice.equals(other.currentMarketPrice))
            return false;
        if (currentPrice == null) {
            if (other.currentPrice != null)
                return false;
        } else if (!currentPrice.equals(other.currentPrice))
            return false;
        if (entryDate == null) {
            if (other.entryDate != null)
                return false;
        } else if (!entryDate.equals(other.entryDate))
            return false;
        if (gainOrLoss == null) {
            if (other.gainOrLoss != null)
                return false;
        } else if (!gainOrLoss.equals(other.gainOrLoss))
            return false;
        if (holding == null) {
            if (other.holding != null)
                return false;
        } else if (!holding.equals(other.holding))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (percentageChange == null) {
            if (other.percentageChange != null)
                return false;
        } else if (!percentageChange.equals(other.percentageChange))
            return false;
        if (portfolioId == null) {
            if (other.portfolioId != null)
                return false;
        } else if (!portfolioId.equals(other.portfolioId))
            return false;
        if (purchaseBrokerage == null) {
            if (other.purchaseBrokerage != null)
                return false;
        } else if (!purchaseBrokerage.equals(other.purchaseBrokerage))
            return false;
        if (purchasePricePerUnit == null) {
            if (other.purchasePricePerUnit != null)
                return false;
        } else if (!purchasePricePerUnit.equals(other.purchasePricePerUnit))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        if (totalPurchasePrice == null) {
            if (other.totalPurchasePrice != null)
                return false;
        } else if (!totalPurchasePrice.equals(other.totalPurchasePrice))
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((currentMarketPrice == null) ? 0 : currentMarketPrice.hashCode());
        result = prime * result + ((currentPrice == null) ? 0 : currentPrice.hashCode());
        result = prime * result + ((entryDate == null) ? 0 : entryDate.hashCode());
        result = prime * result + ((gainOrLoss == null) ? 0 : gainOrLoss.hashCode());
        result = prime * result + ((holding == null) ? 0 : holding.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((percentageChange == null) ? 0 : percentageChange.hashCode());
        result = prime * result + ((portfolioId == null) ? 0 : portfolioId.hashCode());
        result = prime * result + ((purchaseBrokerage == null) ? 0 : purchaseBrokerage.hashCode());
        result = prime * result + ((purchasePricePerUnit == null) ? 0 : purchasePricePerUnit.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        result = prime * result + ((totalPurchasePrice == null) ? 0 : totalPurchasePrice.hashCode());
        return result;
    }

}
