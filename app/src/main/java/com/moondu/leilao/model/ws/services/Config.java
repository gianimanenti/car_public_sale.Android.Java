package com.moondu.leilao.model.ws.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {

    private final Retrofit retrofit;

    public Config() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return new Date(json.getAsLong());
            }
        });

        builder.registerTypeAdapter(Date.class, new JsonSerializer() {
            @Override
            public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(((Date) src).getTime());
            }
        });


        Gson gson = builder.setLenient().create();
        GsonConverterFactory factory = GsonConverterFactory.create(gson);
        RxJava2CallAdapterFactory adapter = RxJava2CallAdapterFactory.create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder().
                readTimeout(360, TimeUnit.SECONDS).
                connectTimeout(360, TimeUnit.SECONDS).build();

        this.retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl("http://cep.moondu.io/ws/")
                .addCallAdapterFactory(adapter).addConverterFactory(factory).build();
    }

    public CepService cepService() {
        return this.retrofit.create(CepService.class);
    }
}
