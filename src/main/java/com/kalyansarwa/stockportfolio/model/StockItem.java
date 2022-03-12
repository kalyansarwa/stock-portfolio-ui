package com.kalyansarwa.stockportfolio.model;

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

    String entryDate;
    String symbol;
    Double price;
    Integer quantity;
    Double totalPrice;

    public StockItem calculate() {
        this.totalPrice = this.price * this.quantity;
        return this;
    }
}
