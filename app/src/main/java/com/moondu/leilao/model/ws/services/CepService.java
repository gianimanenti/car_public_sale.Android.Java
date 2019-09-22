package com.moondu.leilao.model.ws.services;

import com.moondu.leilao.model.ws.Cep;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepService {

    @GET("address/search/{filter}/json")
    Call<List<Cep>> list(@Path("filter") String filter);
}
