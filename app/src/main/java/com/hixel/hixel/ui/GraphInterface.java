package com.hixel.hixel.ui;

import com.hixel.hixel.data.entities.Company;
import java.util.List;

public interface GraphInterface {
    void drawGraph(Company company, String selectedRatio);
    void drawGraph(List<Company> companies, String selectedRatio);
}