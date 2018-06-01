package com.hixel.hixel.network;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.search.SearchEntry;

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
