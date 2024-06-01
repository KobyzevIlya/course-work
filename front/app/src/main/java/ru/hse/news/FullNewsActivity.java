package ru.hse.news;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FullNewsActivity extends AppCompatActivity {

    public static String EXTRA_NEWS_LINK = "news_link";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);

        WebView webView = findViewById(R.id.webViewFullNews);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        String newsLink = getIntent().getStringExtra(EXTRA_NEWS_LINK);
        if (newsLink != null) {
            webView.loadUrl(newsLink);
        }
    }
}
