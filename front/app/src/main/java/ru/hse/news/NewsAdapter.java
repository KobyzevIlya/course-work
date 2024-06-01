package ru.hse.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.hse.news.api.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private OnLikeClickListener onLikeClickListener;

    public NewsAdapter(List<News> newsList, OnLikeClickListener onLikeClickListener) {
        this.newsList = newsList;
        this.onLikeClickListener = onLikeClickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news, onLikeClickListener);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNewsTitle;
        ImageView imageViewNews;
        TextView textViewLikesCount;
        ImageButton buttonLike;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNewsTitle = itemView.findViewById(R.id.textViewNewsTitle);
            imageViewNews = itemView.findViewById(R.id.imageViewNews);
            textViewLikesCount = itemView.findViewById(R.id.textViewLikesCount);
            buttonLike = itemView.findViewById(R.id.buttonLike);
        }

        public void bind(News news, OnLikeClickListener onLikeClickListener) {
            textViewNewsTitle.setText(news.getTitle());
            Picasso.get().load(news.getImage()).into(imageViewNews);
            // Загрузить количество лайков и статус лайка
            // (этот метод должен быть реализован в активности или фрагменте)
            onLikeClickListener.updateLikesCount(news.getId(), textViewLikesCount);
            onLikeClickListener.checkLikeStatus(news.getId(), buttonLike);

            buttonLike.setOnClickListener(v -> onLikeClickListener.onLikeClick(news.getId(), textViewLikesCount, buttonLike));
        }
    }

    public interface OnLikeClickListener {
        void onLikeClick(int newsId, TextView textViewLikesCount, ImageButton buttonLike);
        void updateLikesCount(int newsId, TextView textViewLikesCount);
        void checkLikeStatus(int newsId, ImageButton buttonLike);
    }

    public static String formatLikes(int count) {
        if (count % 10 == 1 && count % 100 != 11) {
            return count + " лайк";
        } else if (count % 10 >= 2 && count % 10 <= 4 && (count % 100 < 10 || count % 100 >= 20)) {
            return count + " лайка";
        } else {
            return count + " лайков";
        }
    }
}
