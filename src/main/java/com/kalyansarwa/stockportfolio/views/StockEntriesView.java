package com.kalyansarwa.stockportfolio.views;

import java.time.LocalDate;

import javax.annotation.security.PermitAll;

import com.kalyansarwa.stockportfolio.model.StockEntry;
import com.kalyansarwa.stockportfolio.service.PortfolioService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@PageTitle("Entries | Stock Portfolio")
@Route(value = "entries", layout = MainLayout.class)
@PermitAll
@Slf4j
public class StockEntriesView extends VerticalLayout {
    Grid<StockEntry> grid = new Grid<>(StockEntry.class);
    TextField filterText = new TextField();

    StockEntryForm form;
    PortfolioService service;

    public StockEntriesView(PortfolioService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(
                getToolbar(),
                getContent());

        updateList();
        closeEditor();

    }

    private void closeEditor() {
        form.setStockItem(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllStockEntries(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
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

        Dialog dialog = new Dialog();

        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        Button stockExitButton = new Button("Exit Stock");
        stockExitButton.addClickListener(e -> dialog.open());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addStockEntryButton, dialog, stockExitButton);
        toolbar.setClassName("toolbar");
        return toolbar;
    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
        dialog.getElement().setAttribute("aria-label", "Stock Exit");

        H2 headline = new H2("Stock Exit");
        headline.getStyle().set("margin-top", "0");

        ComboBox<String> stockSymbol = new ComboBox<>("Stock Symbol");
        stockSymbol.setItems(service.getStockSymbols());
        stockSymbol.setAllowCustomValue(false);

        DatePicker exitDate = new DatePicker("Exit Date");
        NumberField price = new NumberField("Sale Price");

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button saveButton = new Button("Exit",
                e -> stockExit(stockSymbol.getValue(), exitDate.getValue(), price.getValue(), dialog));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                saveButton);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.getStyle().set("margin-top", "var(--lumo-space-m)");

        VerticalLayout dialogLayout = new VerticalLayout(headline,
                stockSymbol,
                exitDate,
                price,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private void stockExit(String symbol, LocalDate exitDate, Double price, Dialog dialog) {
        log.info("symbol : {}, exitDate : {}, price : {}", symbol, exitDate, price);
        service.stockExit(symbol, exitDate, price);
        dialog.close();
    }

    private void addStockItem() {
        grid.asSingleSelect().clear();
        editStockItem(new StockEntry());
    }

    private void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();

        grid.setColumns(
                "entryDate",
                "symbol",
                "purchasePricePerUnit",
                "quantity",
                "purchaseBrokerage",
                "totalPurchasePrice",
                "currentMarketPrice",
                "percentageChange",
                "currentPrice",
                "gainOrLoss");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.asSingleSelect().addValueChangeListener(e -> editStockItem(e.getValue()));

    }

    private void editStockItem(StockEntry stockItem) {
        if (stockItem == null) {
            closeEditor();
        } else {
            form.setStockItem(stockItem);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
