package com.leonardo.carteiravirtual.api;

import com.leonardo.carteiravirtual.models.Currency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CurrencyApi {
    @GET("json/{pares}")
    Call<List<Currency>> obterCotacao(@Path("pares") String pares);
}
