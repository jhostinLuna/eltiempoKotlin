package com.jhostinlh.tiempokotlin.Retrofit

import com.jhostinlh.tiempokotlin.Modelo.Tiempo
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface InterfaceRetrofit {
    @GET("data/2.5/weather")
    fun getTiempo(@Query("q") q: String,@Query("appid") appid:String) : Call<Tiempo>
}