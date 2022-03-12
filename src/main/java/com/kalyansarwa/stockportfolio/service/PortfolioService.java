package com.kalyansarwa.stockportfolio.service;

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
    @Autowired
    private StockRepo stockRepo;

    public List<StockItem> findAll() {
        return stockRepo.findAll().stream()
                .map(StockItem::calculate).collect(Collectors.toList());
    }

    public void save(StockItem s) {
        stockRepo.save(s);
    }

    @PostConstruct
    public void initialize() {
        stockRepo.saveAll(
            Stream.of("12-JAN-2022:Stock1:12:200", "13-JAN-2022:Stock2:23:245", "14-JAN-2022:Stock3:87:156", "15-JAN-2022:Stock4:8:3456", "16-JAN-2022:Stock5:98:47")
            .map(entry -> {
                String[] split = entry.split(":");
                StockItem stockEntry = new StockItem();
                stockEntry.setEntryDate(split[0]);
                stockEntry.setSymbol(split[1]);
                stockEntry.setQuantity(Integer.parseInt(split[2]));
                stockEntry.setPrice(Double.parseDouble(split[3]));
                return stockEntry;
            }).collect(Collectors.toList())
        );
    }
}
