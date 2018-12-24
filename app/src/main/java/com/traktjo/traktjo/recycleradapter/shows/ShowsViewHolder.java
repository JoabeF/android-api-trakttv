package com.traktjo.traktjo.recycleradapter.shows;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.traktjo.traktjo.R;

public class ShowsViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView imageUrl;

    public ShowsViewHolder(View itemView) {
        super(itemView);

        name     = (TextView) itemView.findViewById(R.id.main_name);
        imageUrl = (ImageView) itemView.findViewById(R.id.main_image_url);
    }
}
