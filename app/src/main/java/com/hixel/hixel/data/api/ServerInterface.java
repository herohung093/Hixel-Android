package com.hixel.hixel.data.api;

import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.models.LoginData;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.data.entities.User;

import java.util.ArrayList;

import io.reactivex.Single;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerInterface {

    @POST("/login")
    @Headers("No-Authentication: true")
    Call<Void> login(@Body LoginData request);

    @POST("/users/sign-up")
    @Headers("No-Authentication: true")
    Call<Void> signup(@Body User request);

    @GET("/users/profile")
    Call<User> userData();

    @GET("/users/refresh")
    Call<Void> refreshAccessToken(@Header("Refresh") String Refresh);

    @GET("/companydata")
    Call<ArrayList<Company>> doGetCompanies(@Query("tickers") String tickers, @Query("years") int years);

    @GET("/companydata")
    Call<ArrayList<Company>> getCompanies(@Query("tickers") String tickers, @Query("years") int years);

    @GET("/search")
    Single<List<SearchEntry>> doSearchQuery(@Query("query") String query);

    // TODO: Method is deprecated, remove it from codebase.
    @GET("/meta/ratios")
    Call<ArrayList<String>> doMetaQuery();
}
