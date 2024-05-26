package ru.hse.news.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface ApiService {
    @GET("login")
    Call<LoginResponse> checkLogin();

    @POST("login")
    Call<LoginResponse> loginUser(@Query("login") String login, @Query("password") String password);

    @DELETE("login")
    Call<LoginResponse> logoutUser();
}