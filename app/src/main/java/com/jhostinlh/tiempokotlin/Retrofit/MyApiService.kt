package com.jhostinlh.tiempokotlin.Retrofit

import com.jhostinlh.tiempokotlin.Modelo.PrediccionTiempo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApiService {

    @GET("data/2.5/forecast")
    fun getPrediccion7x3(@Query("q") ciudad: String,
                         @Query("lang") idioma: String,
                         @Query("units") medida: String,
                         @Query("appid") apiKey: String): Call<PrediccionTiempo>
}