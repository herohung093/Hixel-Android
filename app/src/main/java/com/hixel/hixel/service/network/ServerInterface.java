package com.hixel.hixel.service.network;

import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.LoginData;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.service.models.ApplicationUser;

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
    Call<Void> signup(@Body ApplicationUser request);

    @GET("/users/reset-email")
    @Headers("No-Authentication: true")
    Call<Void> resetEmail(@Query("email") String email);

    @GET("/users/profile")
    Call<ApplicationUser> userData();

    @GET("/users/reset-code")
    @Headers("No-Authentication: true")
    Call<Void> resetCode(@Query("email") String email, @Query("code") String code);

    @GET("/users/reset-password")
    @Headers("No-Authentication: true")
    Call<Void> resetPassword(@Query("email") String email, @Query("code") String code, @Query("password") String password);

    @GET("/users/change-password")
    Call<Void> changePassword(@Query("code") String oldPassword, @Query("password") String newPassword);

    @GET("/users/refresh")
    Call<Void> refreshAccessToken(@Header("Refresh") String Refresh);

    @GET("/companydata")
    Call<ArrayList<Company>> doGetCompanies(@Query("tickers") String tickers, @Query("years") int years);

    @GET("/search")
    Single<List<SearchEntry>> doSearchQuery(@Query("query") String query);

    //NOTE: Deprecated
    @GET("/meta/ratios")
    Call<ArrayList<String>> doMetaQuery();
}
