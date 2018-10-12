package com.hixel.hixel.service.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity
public class Portfolio {

    @PrimaryKey
    public final int id; // TODO: Use the CIK for a company as the ID.
    public List<PortfolioCompany> companies;

    public Portfolio(int id, List<PortfolioCompany> companies) {
        this.id = id;
        this.companies = companies;
    }
}
