package com.hixel.hixel.data.api;

import android.arch.lifecycle.LiveData;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.user.Portfolio;
import com.hixel.hixel.data.models.ApplicationUser;
import com.hixel.hixel.data.models.LoginData;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.data.entities.user.User;

import io.reactivex.Single;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * List of endpoints for consuming REST API, these calls relate to User data, Company data,
 * and authorisation.
 */
public interface ServerInterface {

    /**
     * Sends user login data to API, if the user exists returns a 200 response.
     *
     * @param request the LoginData
     * @return void - however use the response code to gauge success
     */
    @POST("/login")
    @Headers("No-Authentication: true")
    Call<Void> login(@Body LoginData request);

    /**
     * Sends the ApplicationUser data for a new user to the API, if the user is created
     * successfully a 200 response code is returned.
     *
     * @param request the ApplicationUser data
     * @return void - however use the response code to gauge success
     */
    @POST("/users/sign-up")
    @Headers("No-Authentication: true")
    Call<Void> signup(@Body ApplicationUser request);

    /**
     * Send a new email string to the API to update the users email.
     *
     * @param email the users new email
     * @return void - however use the response code to gauge success
     */
    @GET("/users/reset-email")
    @Headers("No-Authentication: true")
    Call<Void> resetEmail(@Query("email") String email);

    /**
     * Retrieves the userData for the currently active user.
     *
     * @return a User object for the currently active user.
     */
    @GET("/users/profile")
    Call<User> userData();

    /**
     * Adds a company to the current user's portfolio
     *
     * @param ticker The ticker symbol of the company that the user is adding to their portfolio
     * @return The current user's portfolio
     */
    @POST("/users/portfolio/company")
    Call<Portfolio> addCompany(@Query("ticker") String ticker);

    /**
     * Removes a company from the current user's portfolio
     *
     * @param ticker The ticker symbol of the company that the user is removing from their portfolio
     * @return The current user's portfolio
     */
    @DELETE("/users/portfolio/company")
    Call<Portfolio> removeCompany(@Query("ticker") String ticker);

    /**
     * Sends a reset code and users email
     *
     * @param email the email of the currently active user
     * @param code the code the user has received
     * @return void
     */
    @GET("/users/reset-code")
    @Headers("No-Authentication: true")
    Call<Void> resetCode(@Query("email") String email, @Query("code") String code);

    /**
     * Reset the users password with a reset code as authentication.
     *
     * @param email the email of the currently active user
     * @param code the code the user has received
     * @param password the new password
     * @return void
     */
    @GET("/users/reset-password")
    @Headers("No-Authentication: true")
    Call<Void> resetPassword(@Query("email") String email,
            @Query("code") String code, @Query("password") String password);

    /**
     * API call to change the currently active users password.
     *
     * @param oldPassword the users old password
     * @param newPassword the new password
     * @return void
     */
    @GET("/users/change-password")
    Call<Void> changePassword(@Query("code") String oldPassword,
            @Query("password") String newPassword);

    /**
     * API call to refresh the access token
     *
     * @param refresh refresh token
     * @return void
     */
    @GET("/users/refresh")
    Call<Void> refreshAccessToken(@Header("Refresh") String refresh);

    /**
     * API call to receive an ArrayList of Company objects based upon the sent tickers.
     *
     * @param tickers the tickers of the companies required
     * @param years the number of years of data for each company
     * @return an ArrayList of Company objects
     */
    @GET("/companydata")
    LiveData<ApiResponse<List<Company>>> getCompanies(@Query("tickers") String tickers, @Query("years") int years);

    @GET("/companydata")
    LiveData<ApiResponse<Company>> getCompany(@Query("tickers") String tickers, @Query("years") int years);

    /**
     * API call to query the server for tickers related to a users query
     *
     * @param query the query of the user
     * @return the results of the query
     */
    @GET("/search")
    Single<List<SearchEntry>> doSearchQuery(@Query("query") String query);

}
