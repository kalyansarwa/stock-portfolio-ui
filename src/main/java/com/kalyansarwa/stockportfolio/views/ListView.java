package com.kalyansarwa.stockportfolio.views;

import javax.annotation.security.PermitAll;

import com.kalyansarwa.stockportfolio.model.StockItem;
import com.kalyansarwa.stockportfolio.service.PortfolioService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Entries | Stock Portfolio")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class ListView extends VerticalLayout {
    Grid<StockItem> grid = new Grid<>(StockItem.class);
    TextField filterText = new TextField();

    StockEntryForm form;
    PortfolioService service;

    public ListView(PortfolioService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(
            getToolbar(),
            getContent()
        );

        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setStockItem(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2,grid);
        content.setFlexGrow(1, form);

        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new StockEntryForm();
        form.setWidth("25em");

        form.addListener(StockEntryForm.SaveEvent.class, this::saveStockEntryItem);
        form.addListener(StockEntryForm.DeleteEvent.class, this::deleteStockEntryItem);
        form.addListener(StockEntryForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveStockEntryItem(StockEntryForm.SaveEvent event) {
        service.save(event.getStockItem());
        updateList();
        closeEditor();
    }

    private void deleteStockEntryItem(StockEntryForm.DeleteEvent event) {
        service.deleteEntry(event.getStockItem());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addStockEntryButton = new Button("Add Stock Entry");
        addStockEntryButton.addClickListener(e -> addStockItem());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addStockEntryButton);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private void addStockItem() {
        grid.asSingleSelect().clear();
        editStockItem(new StockItem());
    }

    private void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();

        grid.setColumns(
                "entryDate", 
                "symbol", 
                "marketCap",
                "price",
                "quantity",
                "purchaseCost",
                "totalPrice",
                "currentMarketPrice",
                "percentageChange",
                "currentPrice",
                "gainOrLoss");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editStockItem(e.getValue()));

    }

    private void editStockItem(StockItem stockItem) {
        if (stockItem == null) {
            closeEditor();
        } else {
            form.setStockItem(stockItem);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
