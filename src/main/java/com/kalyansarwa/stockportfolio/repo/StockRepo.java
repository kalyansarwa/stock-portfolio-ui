package com.kalyansarwa.stockportfolio.repo;

import com.kalyansarwa.stockportfolio.model.StockItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<StockItem, Long> {
    
}
