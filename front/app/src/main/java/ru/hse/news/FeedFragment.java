package ru.hse.news;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.news.api.ApiClient;
import ru.hse.news.api.ApiService;
import ru.hse.news.api.News;

public class FeedFragment extends Fragment implements NewsAdapter.OnLikeClickListener {

    private TextView textViewLoginRequired;
    private RecyclerView recyclerViewNews;
    private NewsAdapter newsAdapter;
    private ApiService apiService;
    private EditText editTextSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        textViewLoginRequired = view.findViewById(R.id.textViewLoginRequired);
        recyclerViewNews = view.findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext()));
        editTextSearch = view.findViewById(R.id.editTextSearch);

        apiService = ApiClient.getClient().create(ApiService.class);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Не используется
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Выполнение поиска при изменении текста
                searchNews(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Не используется
            }
        });

        checkLogin();

        return view;
    }

    private void checkLogin() {
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
        recyclerViewNews.setVisibility(View.GONE);
    }

    private void showNews() {
        textViewLoginRequired.setVisibility(View.GONE);
        recyclerViewNews.setVisibility(View.VISIBLE);
        loadAllNews();
    }

    private void loadAllNews() {
        Call<List<News>> call = apiService.getAllNews(null);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    List<News> newsList = response.body();
                    if (newsList != null && !newsList.isEmpty()) {
                        newsAdapter = new NewsAdapter(newsList, FeedFragment.this);
                        recyclerViewNews.setAdapter(newsAdapter);
                    } else {
                        textViewLoginRequired.setText("Список новостей пуст");
                        textViewLoginRequired.setVisibility(View.VISIBLE);
                    }
                } else {
                    textViewLoginRequired.setText("Ошибка загрузки списка новостей: " + response.code());
                    textViewLoginRequired.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                textViewLoginRequired.setText("Ошибка загрузки списка новостей: " + t.getMessage());
                textViewLoginRequired.setVisibility(View.VISIBLE);
            }
        });
    }

    private void searchNews(String query) {
        if (!query.isEmpty()) {
            Call<List<News>> call = apiService.getAllNews(query);
            call.enqueue(new Callback<List<News>>() {
                @Override
                public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                    if (response.isSuccessful()) {
                        List<News> newsList = response.body();
                        if (newsList != null && !newsList.isEmpty()) {
                            newsAdapter = new NewsAdapter(newsList, FeedFragment.this);
                            recyclerViewNews.setAdapter(newsAdapter);
                        } else {
                            textViewLoginRequired.setText("По вашему запросу новостей не найдено");
                            textViewLoginRequired.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textViewLoginRequired.setText("Ошибка поиска новостей: " + response.code());
                        textViewLoginRequired.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<News>> call, Throwable t) {
                    textViewLoginRequired.setText("Ошибка поиска новостей: " + t.getMessage());
                    textViewLoginRequired.setVisibility(View.VISIBLE);
                }
            });
        } else {
            loadAllNews();
        }
    }

    @Override
    public void onLikeClick(int newsId, TextView textViewLikesCount, ImageButton buttonLike) {
        Call<ru.hse.news.api.Response> call = apiService.likeNews(newsId);
        call.enqueue(new Callback<ru.hse.news.api.Response>() {
            @Override
            public void onResponse(Call<ru.hse.news.api.Response> call, Response<ru.hse.news.api.Response> response) {
                if (response.isSuccessful()) {
                    ru.hse.news.api.Response likeResponse = response.body();
                    if (likeResponse != null) {
                        updateLikesCount(newsId, textViewLikesCount);
                        checkLikeStatus(newsId, buttonLike);
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

    @Override
    public void updateLikesCount(int newsId, TextView textViewLikesCount) {
        Call<ru.hse.news.api.Response> call = apiService.getLikesCount(newsId);
        call.enqueue(new Callback<ru.hse.news.api.Response>() {
            @Override
            public void onResponse(Call<ru.hse.news.api.Response> call, Response<ru.hse.news.api.Response> response) {
                if (response.isSuccessful()) {
                    ru.hse.news.api.Response likesResponse = response.body();
                    if (likesResponse != null) {
                        textViewLikesCount.setText(NewsAdapter.formatLikes(Integer.parseInt(likesResponse.getMessage())));
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

    @Override
    public void checkLikeStatus(int newsId, ImageButton buttonLike) {
        Call<ru.hse.news.api.Response> call = apiService.checkLike(newsId);
        call.enqueue(new Callback<ru.hse.news.api.Response>() {
            @Override
            public void onResponse(Call<ru.hse.news.api.Response> call, Response<ru.hse.news.api.Response> response) {
                if (response.isSuccessful()) {
                    ru.hse.news.api.Response likeResponse = response.body();
                    if (likeResponse != null && likeResponse.getResult()) {
                        buttonLike.setImageResource(R.drawable.ic_heart_filled);
                    } else {
                        buttonLike.setImageResource(R.drawable.ic_heart_outline);
                    }
                } else {
                    buttonLike.setImageResource(R.drawable.ic_heart_outline);
                }
            }

            @Override
            public void onFailure(Call<ru.hse.news.api.Response> call, Throwable t) {
                buttonLike.setImageResource(R.drawable.ic_heart_outline);
            }
        });
    }
}
