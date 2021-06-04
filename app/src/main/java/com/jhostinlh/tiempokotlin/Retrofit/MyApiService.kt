package com.jhostinlh.tiempokotlin.Retrofit

import Dia
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

    @GET("data/2.5/onecall")
    fun getPrediccionDias(@Query("lat") latitud: String,
                          @Query("lon") longitud:String,
                          @Query("exclude") excluye: String,
                          @Query("units") unidad:String,
                          @Query("lang") lengua:String,
                          @Query("appid") apiKey:String ): Call<Dia>
}