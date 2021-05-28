package com.jhostinlh.tiempokotlin.Modelo


import com.google.gson.annotations.SerializedName

data class PrediccionTiempo(
    @SerializedName("city")
    val city: City?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("list")
    val lista: ArrayList<Lista>?,
    @SerializedName("message")
    val message: Int?
)