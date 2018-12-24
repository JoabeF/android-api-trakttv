package com.traktjo.traktjo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.traktjo.traktjo.R;
import com.traktjo.traktjo.helper.JsonFunctions;
import com.traktjo.traktjo.helper.RequestOmdb;

import java.util.concurrent.ExecutionException;

public class ShowsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String imdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);

        imdb = getIntent().getExtras().getString("imdb");

        inflateToolbar();

        RequestOmdb request = new RequestOmdb();
        request.execute(imdb);

        try {
            String json =  request.get();
            populateView(json);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void populateView(String json) {

        if(json == null) {
            Toast.makeText(this, "Algo deu errado. Verifique sua conexão", Toast.LENGTH_SHORT).show();
            return;
        }

        final ImageView imageView = (ImageView) findViewById(R.id.show_image);
        TextView title      = (TextView)  findViewById(R.id.show_title);
        TextView rating     = (TextView)  findViewById(R.id.show_rate);
        TextView genre      = (TextView)  findViewById(R.id.show_genre);
        TextView sinopse    = (TextView)  findViewById(R.id.show_sinopse);
        TextView runtime    = (TextView)  findViewById(R.id.show_runtime);


        JsonFunctions jsonFunctions = new JsonFunctions(json);
        final String imageStr   = jsonFunctions.getJsonValue("Poster");
        String titleStr   = jsonFunctions.getJsonValue("Title");
        String ratingStr  = jsonFunctions.getJsonValue("imdbRating");
        String genreStr   = jsonFunctions.getJsonValue("Genre");
        String sinopseStr = jsonFunctions.getJsonValue("Plot");
        String runtimeStr = jsonFunctions.getJsonValue("Runtime");

//        Picasso.get()
//                .load(imageStr)
//                .resize(404, 600)
//                .error(R.drawable.ic_block)
//                .placeholder(R.drawable.animation_rotate)
//                .into(imageView);

        Picasso.get()
                .load(imageStr)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(404, 600)
                .error(R.drawable.ic_block)
                .placeholder(R.drawable.animation_rotate)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(imageStr)
                                .resize(404, 600)
                                .error(R.drawable.ic_block)
                                .placeholder(R.drawable.animation_rotate)
                                .into(imageView);
                    }
                });

        title.setText(titleStr);
        rating.setText(ratingStr);
        genre.setText(genreStr);
        sinopse.setText(sinopseStr);
        runtime.setText(runtimeStr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inflateToolbar() {

        toolbar = (Toolbar) findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("About");
    }
}