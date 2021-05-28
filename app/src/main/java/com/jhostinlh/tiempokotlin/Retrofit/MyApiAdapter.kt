package com.jhostinlh.tiempokotlin.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApiAdapter {
    companion object {
        private var API_SERVICE: MyApiService? = null
        const val BASE_URL = "https://api.openweathermap.org/"
        fun getApiService(): MyApiService ? {

            if (API_SERVICE == null) {
                val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                API_SERVICE = retrofit.create(MyApiService::class.java)

            }
            return API_SERVICE
        }
    }
}