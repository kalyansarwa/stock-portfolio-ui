package com.kalyansarwa.stockportfolio.views;

import com.kalyansarwa.stockportfolio.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    private SecurityService service;
    
    public MainLayout(SecurityService service) {
        this.service = service;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Stock Portfolio");
        logo.addClassNames("text-l","m-m");

        Button logoutButton = new Button("Log Out", e -> service.logout());
        Label label = new Label(service.getAuthenticatedUser().getUsername());
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, label, logoutButton);

        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawer() {
        RouterLink stockEntriesView = new RouterLink("Stock Entries", StockEntriesView.class);
        stockEntriesView.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink stockExitsView = new RouterLink("Stock Exits", StockExitsView.class);
        stockExitsView.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
            new RouterLink("Dashboard", DashboardView.class),
            stockEntriesView,
            stockExitsView
        ));
    }
    
}
