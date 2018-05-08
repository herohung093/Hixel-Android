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
        companies.add(new Company("Apple", "AAPL", 0.61, 0.75, 1.5));
        companies.add(new Company("Tesla", "TSLA", 0.82, 1.5, 1.2));
        companies.add(new Company("Twitter", "TWTR", 0.30, 1.2, 2.2));
        companies.add(new Company("Snapchat", "SNAP", 0.54, 0.4, 0.3));
        companies.add(new Company("Facebook", "FB", 0.25, 1.5, 1.5));
        companies.add(new Company("Berkshire Hathaway", "BRK.A",0.1, 2.0, 2.0));
        companies.add(new Company("Wells Fargo", "WFC", 0.2, 2.0, 2.0));
        companies.add(new Company("Walmart", "WMT", 0.6, 2.0, 2.0));
        companies.add(new Company("Kraft Heinz Co", "KHC", 0.2, 2.0, 2.0));
        companies.add(new Company("Ford Motor", "F", 0.01, 1.0, 1.0));

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
        Collections.reverse(portfolio.getCompanies());
    }

    @Override
    public void sortByLiquidity() {
        Collections.sort(portfolio.getCompanies(),
                (c1, c2) -> Double.compare(c1.getLiquidity(), c2.getLiquidity()));
        Collections.reverse(portfolio.getCompanies());
    }
}
