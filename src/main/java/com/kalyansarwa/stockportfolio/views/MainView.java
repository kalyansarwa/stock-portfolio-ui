package com.kalyansarwa.stockportfolio.views;

import com.kalyansarwa.stockportfolio.model.StockItem;
import com.kalyansarwa.stockportfolio.service.PortfolioService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Flow")
@Route(value = "")
public class MainView extends VerticalLayout {
    
    private PortfolioService service;
    private Grid<StockItem> grid;
    private Editor editor;

    public MainView(PortfolioService service) {

        this.service = service;

        grid = new Grid<>(StockItem.class);
        editor = new Editor();
        // add toolbar: Switch to Exit View
        Button showExits = new Button("Switch to Exit View");
        HorizontalLayout toolbar = new HorizontalLayout(showExits);
	
        add(toolbar, editor, grid);

        grid.setColumns("entryDate", "symbol", "price", "quantity", "totalPrice");
        grid.asSingleSelect().addValueChangeListener(event -> {
            editor.setStockItem(event.getValue());
        });
        listStockEntries();
    }

    private void listStockEntries() {
        grid.setItems(service.findAll());
    }

    private void addStockItem() {
        grid.asSingleSelect().clear();
        editor.setStockItem(new StockItem());
    }

    class Editor extends FormLayout {
        TextField entryDate = new TextField("Entry Date");
        TextField symbol = new TextField("Stock Symbol");
        NumberField price = new NumberField("Price");
        IntegerField quantity = new IntegerField("Quantity");

        Binder<StockItem> binder = new Binder<>(StockItem.class);

        private StockItem stockItem;

        Editor() {

            Button saveButton = new Button("Save");
            Button addButton = new Button("Add");
            binder.bindInstanceFields(this);

            add(entryDate, symbol, price, quantity, saveButton, addButton);

            saveButton.addClickListener(click -> {
                try {
                    binder.writeBean(stockItem);
                    service.save(stockItem);
                    listStockEntries();
                    binder.readBean(new StockItem());
                } catch (Exception e) {
                    System.out.println("Error here " + e.getMessage());
                    e.printStackTrace();
                }
            });

            addButton.addClickListener(click -> addStockItem());
        }

        void setStockItem(StockItem stockItem) {
            this.stockItem = stockItem;
            binder.readBean(stockItem);
        }
    }
}
