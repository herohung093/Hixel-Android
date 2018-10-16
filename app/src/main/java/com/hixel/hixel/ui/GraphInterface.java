package com.hixel.hixel.ui;

import com.hixel.hixel.data.entities.Company;
import java.util.List;

public interface GraphInterface {
    public void drawGraph(Company company, String selectedRatio);
    public void drawGraph(List<Company> companies, String selectedRatio);
}