package com.kalyansarwa.stockportfolio.views;

import javax.annotation.security.PermitAll;

import com.kalyansarwa.stockportfolio.model.StockExit;
import com.kalyansarwa.stockportfolio.service.PortfolioService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Exits | Stock Portfolio")
@Route(value = "exits", layout = MainLayout.class)
@PermitAll
public class StockExitsView extends VerticalLayout {
    Grid<StockExit> grid = new Grid<>(StockExit.class);
    PortfolioService service;

    public StockExitsView(PortfolioService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        add(grid);
        grid.setItems(service.findAllStockExits());

    }

    private void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();

        grid.setColumns(
                "entryDate",
                "symbol",
                "purchasePricePerUnit",
                "quantity",
                "totalPurchasePrice",
                "exitDate",
                "salePricePerUnit",
                "saleBrokerage",
                "totalSalePrice",
                "preTaxGain");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

    }
}
