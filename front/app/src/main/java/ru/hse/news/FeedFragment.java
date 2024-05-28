package ru.hse.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.news.api.ApiClient;
import ru.hse.news.api.ApiService;
import ru.hse.news.api.News;

public class FeedFragment extends Fragment {

    private TextView textViewLoginRequired;
    private TextView textViewNewsTitle;
    private ImageView imageViewNews;
    private TextView textViewLikesCount;
    private Button buttonLike;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        textViewLoginRequired = view.findViewById(R.id.textViewLoginRequired);
        textViewNewsTitle = view.findViewById(R.id.textViewNewsTitle);
        imageViewNews = view.findViewById(R.id.imageViewNews);
        textViewLikesCount = view.findViewById(R.id.textViewLikesCount);
        buttonLike = view.findViewById(R.id.buttonLike);

        checkLogin();

        return view;
    }

    private void checkLogin() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ru.hse.news.api.Response> call = apiService.checkLogin();
        call.enqueue(new Callback<ru.hse.news.api.Response>() {
            @Override
            public void onResponse(Call<ru.hse.news.api.Response> call, Response<ru.hse.news.api.Response> response) {
                if (response.isSuccessful()) {
                    ru.hse.news.api.Response loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getResult()) {
                        showNews();
                    } else {
                        showLoginRequiredMessage();
                    }
                } else {
                    showLoginRequiredMessage();
                }
            }

            @Override
            public void onFailure(Call<ru.hse.news.api.Response> call, Throwable t) {
                showLoginRequiredMessage();
            }
        });
    }

    private void showLoginRequiredMessage() {
        textViewLoginRequired.setVisibility(View.VISIBLE);
    }

    private void showNews() {
        textViewLoginRequired.setVisibility(View.GONE);
        textViewNewsTitle.setVisibility(View.VISIBLE);
        imageViewNews.setVisibility(View.VISIBLE);
        textViewLikesCount.setVisibility(View.VISIBLE);
        buttonLike.setVisibility(View.VISIBLE);

        loadAllNews();
    }

    private void loadAllNews() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<News>> call = apiService.getAllNews();
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    List<News> newsList = response.body();
                    if (newsList != null && !newsList.isEmpty()) {
                        // Отображаем первую новость из списка
                        displayNews(newsList.get(0));
                    } else {
                        textViewNewsTitle.setText("Список новостей пуст");
                    }
                } else {
                    textViewNewsTitle.setText("Ошибка загрузки списка новостей: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                textViewNewsTitle.setText("Ошибка загрузки списка новостей: " + t.getMessage());
            }
        });
    }

    private void displayNews(News news) {
        textViewNewsTitle.setText(news.getTitle());
        Picasso.get().load(news.getImage()).into(imageViewNews);
        updateLikesCount(news.getId());
        checkLikeStatus(news.getId());
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLike(news.getId());
            }
        });
    }

    private void updateLikesCount(int newsId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ru.hse.news.api.Response> call = apiService.getLikesCount(newsId);
        call.enqueue(new Callback<ru.hse.news.api.Response>() {
            @Override
            public void onResponse(Call<ru.hse.news.api.Response> call, Response<ru.hse.news.api.Response> response) {
                if (response.isSuccessful()) {
                    ru.hse.news.api.Response likesResponse = response.body();
                    if (likesResponse != null) {
                        textViewLikesCount.setText(likesResponse.getMessage() + " лайков");
                    } else {
                        textViewLikesCount.setText("Ошибка загрузки лайков");
                    }
                } else {
                    textViewLikesCount.setText("Ошибка загрузки лайков");
                }
            }

            @Override
            public void onFailure(Call<ru.hse.news.api.Response> call, Throwable t) {
                textViewLikesCount.setText("Ошибка загрузки лайков: " + t.getMessage());
            }
        });
    }

    private void checkLikeStatus(int newsId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ru.hse.news.api.Response> call = apiService.checkLike(newsId);
        call.enqueue(new Callback<ru.hse.news.api.Response>() {
            @Override
            public void onResponse(Call<ru.hse.news.api.Response> call, Response<ru.hse.news.api.Response> response) {
                if (response.isSuccessful()) {
                    ru.hse.news.api.Response likeResponse = response.body();
                    if (likeResponse != null && likeResponse.getResult()) {
                        buttonLike.setText("Удалить лайк");
                    } else {
                        buttonLike.setText("Лайк");
                    }
                } else {
                    buttonLike.setText("Лайк");
                }
            }

            @Override
            public void onFailure(Call<ru.hse.news.api.Response> call, Throwable t) {
                buttonLike.setText("Лайк");
            }
        });
    }

    private void toggleLike(int newsId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ru.hse.news.api.Response> call = apiService.likeNews(newsId);
        call.enqueue(new Callback<ru.hse.news.api.Response>() {
            @Override
            public void onResponse(Call<ru.hse.news.api.Response> call, Response<ru.hse.news.api.Response> response) {
                if (response.isSuccessful()) {
                    ru.hse.news.api.Response likeResponse = response.body();
                    if (likeResponse != null) {
                        textViewLikesCount.setText(likeResponse.getMessage());
                        checkLikeStatus(newsId);
                        updateLikesCount(newsId);
                    } else {
                        textViewLikesCount.setText("Ошибка изменения лайка");
                    }
                } else {
                    textViewLikesCount.setText("Ошибка изменения лайка");
                }
            }

            @Override
            public void onFailure(Call<ru.hse.news.api.Response> call, Throwable t) {
                textViewLikesCount.setText("Ошибка изменения лайка: " + t.getMessage());
            }
        });
    }
}
