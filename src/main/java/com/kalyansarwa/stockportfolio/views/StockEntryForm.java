package com.kalyansarwa.stockportfolio.views;

import com.kalyansarwa.stockportfolio.model.StockItem;
import com.kalyansarwa.stockportfolio.utils.DoubleToBigDecimalConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class StockEntryForm extends FormLayout {

    DatePicker entryDate = new DatePicker("Entry Date");
    TextField symbol = new TextField("Stock Symbol");
    NumberField price = new NumberField("Price");
    IntegerField quantity = new IntegerField("Quantity");

    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button cancelButton = new Button("Cancel");

    Binder<StockItem> binder = new Binder<>(StockItem.class);
    private StockItem stockItem;

    // private StockItem stockItem;

    public StockEntryForm() {
        addClassName("stock-entry-form");

        bindFields();

        add(
                entryDate,
                symbol,
                price,
                quantity,
                createButtonLayout());

        // saveButton.addClickListener(click -> {
        // try {
        // binder.writeBean(stockItem);
        // service.save(stockItem);
        // listStockEntries();
        // binder.readBean(new StockItem());
        // } catch (Exception e) {
        // System.out.println("Error here " + e.getMessage());
        // e.printStackTrace();
        // }
        // });

        // addButton.addClickListener(click -> addStockItem());
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
        binder.readBean(stockItem);
    }

    private void bindFields() {
        binder.forField(price)
                .withConverter(new DoubleToBigDecimalConverter())
                .bind(StockItem::getPrice, StockItem::setPrice);
        binder.forField(entryDate)
                .bind(StockItem::getEntryDate, StockItem::setEntryDate);
        binder.forField(symbol)
                .bind(StockItem::getSymbol, StockItem::setSymbol);
        binder.forField(quantity)
                .bind(StockItem::getQuantity, StockItem::setQuantity);

    }

    private Component createButtonLayout() {

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, stockItem)));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(saveButton, deleteButton, cancelButton);

    }

    private void validateAndSave() {
        try {
            binder.writeBean(stockItem);
            fireEvent(new SaveEvent(this, stockItem));
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

    // Events
    public static abstract class StockEntryFormEvent extends ComponentEvent<StockEntryForm> {
        private StockItem StockItem;

        protected StockEntryFormEvent(StockEntryForm source, StockItem StockItem) {
            super(source, false);
            this.StockItem = StockItem;
        }

        public StockItem getStockItem() {
            return StockItem;
        }
    }

    public static class SaveEvent extends StockEntryFormEvent {
        SaveEvent(StockEntryForm source, StockItem StockItem) {
            super(source, StockItem);
        }
    }

    public static class DeleteEvent extends StockEntryFormEvent {
        DeleteEvent(StockEntryForm source, StockItem StockItem) {
            super(source, StockItem);
        }

    }

    public static class CloseEvent extends StockEntryFormEvent {
        CloseEvent(StockEntryForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
            ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
