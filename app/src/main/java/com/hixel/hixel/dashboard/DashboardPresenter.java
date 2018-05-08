package com.hixel.hixel.dashboard;

import android.graphics.Color;
import com.hixel.hixel.data.Company;
import com.hixel.hixel.data.Portfolio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class DashboardPresenter implements DashboardContract.Presenter {

    private Portfolio portfolio;
    private final DashboardContract.View mDashboardView;

    DashboardPresenter(DashboardContract.View dashboardView) {
        this.mDashboardView = dashboardView;
        mDashboardView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPortfolio();
        populateGraph();
    }

    @Override
    public void populateGraph() {
        mDashboardView.showMainGraph(portfolio.getCompanies());
    }

    private void loadPortfolio() {
        ArrayList<Company> companies = new ArrayList<>();
        companies.add(new Company("Apple", "AAPL", 0.61));
        companies.add(new Company("Tesla", "AAPL", 0.82));
        companies.add(new Company("Twitter", "AAPL", 0.30));
        companies.add(new Company("Snapchat", "AAPL", 0.54));
        companies.add(new Company("Facebook", "AAPL", 0.25));
        this.portfolio = new Portfolio(companies);
    }

    @Override
    public ArrayList<Company> getCompanies() {
        return portfolio.getCompanies();
    }

    @Override
    public int setHealthColor(int position) {
        int color = Color.parseColor("#FFB75D");

        if (portfolio.getCompanies().get(position).getHealth() < 0.5) {
            color = Color.parseColor("#C23934");
        } else if (portfolio.getCompanies().get(position).getHealth() > 0.6) {
            color = Color.parseColor("#4BCA81");
        }
        return color;
    }

    @Override
    public void sortCompanies(String item) {
        switch (item) {
            case "Health":
                sortByHealth();
                break;
            case "Leverage":
                sortByLeverage();
                break;
            case "Liquidity":
                sortByLiquidity();
                break;
        }
    }

    @Override
    public void sortByHealth() {
        Collections.sort(portfolio.getCompanies(),
                (c1, c2) -> Double.compare(c1.getHealth(), c2.getHealth()));
        Collections.reverse(portfolio.getCompanies());
    }

    @Override
    public void sortByLeverage() {
        Collections.sort(portfolio.getCompanies(),
                (c1, c2) -> Double.compare(c1.getLeverage(), c2.getLeverage()));
    }

    @Override
    public void sortByLiquidity() {
        Collections.sort(portfolio.getCompanies(),
                (c1, c2) -> Double.compare(c1.getLiquidity(), c2.getLiquidity()));
    }
}
