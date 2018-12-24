package com.traktjo.traktjo.helper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class JsonFunctions {

    private String json;

    public JsonFunctions(String json) {
        this.json = json;
    }

    public JsonArray stringToJson() {

        Gson gson = new Gson();
        JsonElement element = gson.fromJson (json, JsonElement.class);
        JsonArray jsonArray = element.getAsJsonArray();

        return jsonArray;
    }

    public String getJsonValue (String value) {

        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson (json, JsonObject.class);
        JsonElement jsonElement = jsonObj.get(value);

        return jsonElement.getAsString();
    }
}
