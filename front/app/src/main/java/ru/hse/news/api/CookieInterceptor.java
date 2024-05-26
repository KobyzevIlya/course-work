package ru.hse.news.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieInterceptor implements Interceptor, CookieJar {
    private Map<String, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        List<Cookie> cookies = Cookie.parseAll(HttpUrl.parse(response.request().url().toString()), response.headers());
        if (cookies != null) {
            cookieStore.put(response.request().url().host(), cookies);
        }
        return response;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies != null ? cookies : new ArrayList<>();
    }
}


