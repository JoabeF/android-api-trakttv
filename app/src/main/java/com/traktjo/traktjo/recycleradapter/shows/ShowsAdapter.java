package com.traktjo.traktjo.recycleradapter.shows;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.traktjo.traktjo.R;
import com.traktjo.traktjo.activity.ShowsActivity;
import com.traktjo.traktjo.config.Contants;
import com.traktjo.traktjo.model.Show;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter {

    private List<Show> shows;
    private Context context;
    public OnBottomReachedListener onBottomReachedListener;

    public ShowsAdapter(List<Show> shows, Context context) {
        this.shows = shows;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_show, parent, false);
        ShowsViewHolder holder = new ShowsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ShowsViewHolder myHolder = (ShowsViewHolder) holder;

        Show show = new Show();
        show = shows.get(position);

        final UriComponentsBuilder builder;
        builder = UriComponentsBuilder.fromHttpUrl(Contants.URL_OMDB_IMG)
                .queryParam("i", show.getIds().getImdb())
                .queryParam("apikey", Contants.API_OMDB_KEY);


        myHolder.name.setText(show.getTitle());


        Picasso.get()
                .load(builder.toUriString())
                .resize(404, 600)
                .error(R.drawable.ic_block)
                .placeholder(R.drawable.animation_rotate)
                .into(myHolder.imageUrl);


        final Show finalShow = show;
        myHolder.imageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowsActivity.class);
                intent.putExtra("imdb", finalShow.getIds().getImdb());
                context.startActivity(intent);
            }
        });

        if (position == shows.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public interface OnBottomReachedListener {
        void onBottomReached(int position);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }
}
