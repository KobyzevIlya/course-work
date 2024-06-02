package ru.hse.news.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("login")
    Call<Response> checkLogin();

    @POST("login")
    Call<Response> loginUser(@Query("login") String login, @Query("password") String password);

    @DELETE("login")
    Call<Response> logoutUser();

    @POST("login/register")
    Call<Response> registerUser(@Query("login") String login, @Query("password") String password);

    @GET("news/{id}")
    Call<News> getNews(@Path("id") int newsId);

    @GET("news/{id}/likes_count")
    Call<Response> getLikesCount(@Path("id") int newsId);

    @GET("news/{id}/check_like")
    Call<Response> checkLike(@Path("id") int newsId);

    @POST("news/{id}/like")
    Call<Response> likeNews(@Path("id") int newsId);

    @GET("news.json")
    Call<List<News>> getAllNews(@Query("title") String title);
}
