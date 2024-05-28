package ru.hse.news.api;

import com.google.gson.annotations.SerializedName;

public class News {
    private int id;
    private String title;
    private String image;
    private String publisher;
    private String text;
    private String createdAt;
    private String updatedAt;
    private String url;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }
}
