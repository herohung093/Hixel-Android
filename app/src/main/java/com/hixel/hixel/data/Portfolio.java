package com.hixel.hixel.data;


import java.util.ArrayList;

public class Portfolio {
    ArrayList<Company> companies;

    public Portfolio(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public Company getCompany(int position) {
        return companies.get(position);
    }

    public ArrayList<Company> getCompanies() {
        return this.companies;
    }

}
