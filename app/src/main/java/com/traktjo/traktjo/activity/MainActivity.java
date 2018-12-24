package com.traktjo.traktjo.activity;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.traktjo.traktjo.helper.Connection;
import com.traktjo.traktjo.R;
import com.traktjo.traktjo.helper.GridSpacingItemDecoration;
import com.traktjo.traktjo.helper.JsonFunctions;
import com.traktjo.traktjo.model.Show;
import com.traktjo.traktjo.recycleradapter.shows.ShowsAdapter;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements InternetConnectivityListener {

    private Connection connection;
    private RecyclerView recyclerView;
    private ShowsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Show> shows;
    private Parcelable recyclerViewState;
    private ProgressBar progressBar;
    private InternetAvailabilityChecker listenerInternet;
    private int page = 1;
    private boolean loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        if(!loaded)
            showProgress(true);

        listenerInternet.addInternetConnectivityListener(new InternetConnectivityListener() {
            @Override
            public void onInternetConnectivityChanged(boolean isConnected) {
                if (isConnected) {
                    connect();
                    Toast.makeText(MainActivity.this, "Conectado!", Toast.LENGTH_SHORT).show();
                }else {

                    showProgress(false);
                    Toast.makeText(MainActivity.this, "Sem internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialize(){

        int spanCount = 3;
        int spacing = 16;
        int numberOfColuns = 3;
        boolean includeEdge = true;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_series);
        layoutManager = new GridLayoutManager(this, numberOfColuns);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setLayoutManager(layoutManager);

        InternetAvailabilityChecker.init(MainActivity.this);
        listenerInternet = InternetAvailabilityChecker.getInstance();
        listenerInternet.addInternetConnectivityListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        shows = new ArrayList<Show>();
        shows.clear();
    }

    private void connect () {

        try {

            connection = new Connection();
            connection.execute(page);

            ResponseEntity<String> response = connection.get();
            JsonFunctions jsonFunc;
            JsonArray jsonArray;

            if (response.getStatusCode() == HttpStatus.OK) {

                jsonFunc = new JsonFunctions(response.getBody());
                jsonArray = jsonFunc.stringToJson();

                populateRecycler(jsonArray);

            }else {
                Toast.makeText(this, "Erro! Tente mais tarde.", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void populateRecycler(JsonArray jsonArray) {

        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();//salvar estado do recycler (carregar e não voltar ao incio)

        for (int i = 0; i < jsonArray.size(); i++ ) {

            Gson gson = new Gson();
            JsonElement element = jsonArray.get(i);
            Show show = gson.fromJson(element, Show.class);

            shows.add(show);
        }

        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);//retomar posição do recycler

        adapter = new ShowsAdapter(shows, this);
        recyclerView.setAdapter(adapter);

        bottomListener(adapter);
        loaded = true;
        showProgress(false);
    }

    private void bottomListener (ShowsAdapter adapter) {
        adapter.setOnBottomReachedListener(new ShowsAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                loadMore();
            }
        });
    }

    public void loadMore() {

        try {

            JsonFunctions jsonFunc = null;
            JsonArray jsonArray = null;

            page = page + 1;

            Connection connection = new Connection();
            connection.execute(page);

            ResponseEntity<String> response = connection.get();

            if(response == null)
                return;

            if (response.getStatusCode() == HttpStatus.OK) {

                jsonFunc = new JsonFunctions(response.getBody());
                jsonArray = jsonFunc.stringToJson();

                populateRecycler(jsonArray);

            }else {
                Toast.makeText(this, "Erro! Atualize o app ou tente mais tarde.", Toast.LENGTH_SHORT).show();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    private void showProgress(final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {}

}
