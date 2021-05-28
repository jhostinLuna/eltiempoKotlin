package com.jhostinlh.tiempokotlin.Modelo


import com.google.gson.annotations.SerializedName

data class Lista(
    @SerializedName("clouds")
    val clouds: Clouds?,
    val dt: String?,
    @SerializedName("dt_txt")
    val dtTxt: String?,
    @SerializedName("main")
    val cmain: Cmain?,

    @SerializedName("weather")
    val weather: ArrayList<Weather>?,
    @SerializedName("wind")
    val wind: Wind?
)