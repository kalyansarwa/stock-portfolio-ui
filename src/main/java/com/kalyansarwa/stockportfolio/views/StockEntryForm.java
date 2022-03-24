package com.kalyansarwa.stockportfolio.views;

import com.kalyansarwa.stockportfolio.model.StockEntry;
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

    Binder<StockEntry> binder = new Binder<>(StockEntry.class);
    private StockEntry stockItem;

    public StockEntryForm() {
        addClassName("stock-entry-form");

        bindFields();

        add(
                entryDate,
                symbol,
                price,
                quantity,
                createButtonLayout());

    }

    public void setStockItem(StockEntry stockItem) {
        this.stockItem = stockItem;
        binder.readBean(stockItem);
    }

    private void bindFields() {
        binder.forField(price)
                .withConverter(new DoubleToBigDecimalConverter())
                .bind(StockEntry::getPurchasePricePerUnit, StockEntry::setPurchasePricePerUnit);
        binder.forField(entryDate)
                .bind(StockEntry::getEntryDate, StockEntry::setEntryDate);
        binder.forField(symbol)
                .bind(StockEntry::getSymbol, StockEntry::setSymbol);
        binder.forField(quantity)
                .bind(StockEntry::getQuantity, StockEntry::setQuantity);

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
        private StockEntry StockItem;

        protected StockEntryFormEvent(StockEntryForm source, StockEntry StockItem) {
            super(source, false);
            this.StockItem = StockItem;
        }

        public StockEntry getStockItem() {
            return StockItem;
        }
    }

    public static class SaveEvent extends StockEntryFormEvent {
        SaveEvent(StockEntryForm source, StockEntry StockItem) {
            super(source, StockItem);
        }
    }

    public static class DeleteEvent extends StockEntryFormEvent {
        DeleteEvent(StockEntryForm source, StockEntry StockItem) {
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
