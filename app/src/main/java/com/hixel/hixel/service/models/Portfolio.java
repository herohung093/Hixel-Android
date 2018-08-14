package com.hixel.hixel.service.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
// Portfolio class is not being used. Can we remove it?
public class Portfolio implements Serializable {
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
