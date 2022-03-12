package com.kalyansarwa.stockportfolio.repo;

import java.util.List;

import com.kalyansarwa.stockportfolio.model.StockItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepo extends JpaRepository<StockItem, Long> {

    @Query("select c from StockItem c " +
            "where lower(c.symbol) like lower(concat('%', :searchTerm, '%')) ")
    List<StockItem> search(@Param("searchTerm") String searchTerm);
}
