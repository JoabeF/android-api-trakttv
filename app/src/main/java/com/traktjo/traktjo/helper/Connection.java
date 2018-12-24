package com.traktjo.traktjo.helper;

import android.os.AsyncTask;

import com.traktjo.traktjo.config.Contants;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.UnknownHostException;

public class Connection extends AsyncTask<Integer, Void, ResponseEntity<String>>{

    @Override
    protected ResponseEntity<String> doInBackground(Integer... integers) {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<?> entity;
        UriComponentsBuilder builder;
        ResponseEntity<String> response;

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("trakt-api-version", "2");
        headers.set("trakt-api-key", Contants.CLINET_ID);

        entity = new HttpEntity<>(headers);

        builder = UriComponentsBuilder.fromHttpUrl(Contants.URL_API_TRAKT_POPULAR)
                .queryParam("page", integers[0])
                .queryParam("limit", 16);

        try {
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);

            return response;
        }catch (ResourceAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
