package com.traktjo.traktjo.helper;

import android.os.AsyncTask;

import com.traktjo.traktjo.config.Contants;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RequestOmdb extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... imdb) {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity;
        UriComponentsBuilder builder;
        HttpEntity<String> response;

        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        entity = new HttpEntity<>(headers);

        builder = UriComponentsBuilder.fromHttpUrl(Contants.URL_OMDB)
                .queryParam("i", imdb[0])
                .queryParam("apikey", Contants.API_OMDB_KEY);
        try{
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);

            return response.getBody();
        }catch (ResourceAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
