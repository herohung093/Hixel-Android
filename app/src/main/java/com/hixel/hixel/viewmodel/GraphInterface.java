package com.hixel.hixel.viewmodel;

import com.hixel.hixel.service.models.Company;
import java.util.ArrayList;

public interface GraphInterface {
    public void drawGraph(Company company,String selectedRatio);
    public void drawGraph(ArrayList<Company> companies,String selectedRatio);
}
