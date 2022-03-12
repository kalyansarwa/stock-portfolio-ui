package com.kalyansarwa.stockportfolio.views;

import javax.annotation.security.PermitAll;

import com.kalyansarwa.stockportfolio.service.PortfolioService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Stock Portfolio")
@PermitAll
public class DashboardView extends VerticalLayout {
    
    private PortfolioService service;
    public DashboardView(PortfolioService service) {

        this.service = service;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getStockStats());
    }

    private Component getStockStats() {
        Span stats = new Span(service.countStocks() + "Stocks");
        stats.addClassNames("text-xl","mt-m");
        return stats;
    }
}
