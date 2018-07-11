package com.hixel.hixel.service.network;

import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.SearchEntry;

import java.util.ArrayList;

import io.reactivex.Single;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerInterface {

    @GET("/companydata")
    Call<ArrayList<Company>> doGetCompanies(@Query("tickers") String tickers, @Query("years") int years);

    @GET("/search")
    Single<List<SearchEntry>> doSearchQuery(@Query("query") String query);

    @GET("/meta/ratios")
    Call<ArrayList<String>> doMetaQuery();
}
