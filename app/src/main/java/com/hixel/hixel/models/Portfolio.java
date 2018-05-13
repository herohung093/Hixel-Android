package com.hixel.hixel.models;

import java.util.ArrayList;

public class Portfolio {
    private ArrayList<Company> companies;

    public Portfolio() {
        this.companies = new ArrayList<>();
    }

    public Portfolio(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public Company getCompany(int position) {
        return companies.get(position);
    }

    public ArrayList<Company> getCompanies() {
        return this.companies;
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }
}
