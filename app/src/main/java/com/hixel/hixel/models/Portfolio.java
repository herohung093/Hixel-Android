package com.hixel.hixel.models;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private List<Company> companies;

    public Portfolio() {
        this.companies = new ArrayList<>();
    }

    public Portfolio(List<Company> companies) {
        this.companies = companies;
    }

    public Company getCompany(int position) {
        return companies.get(position);
    }

    public List<Company> getCompanies() {
        return this.companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
