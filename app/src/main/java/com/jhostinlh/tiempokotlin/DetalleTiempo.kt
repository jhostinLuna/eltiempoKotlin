package com.jhostinlh.tiempokotlin

import Dia
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jhostinlh.tiempokotlin.Recycler.RecyclerPrediccionTiempo
import com.jhostinlh.tiempokotlin.databinding.ActivityDetalleTiempoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

const val API_KEY = "4e719a84e03ef9f6ef6f38418f24e0bb"
    const val URL_ICON ="https://openweathermap.org/img/wn/";//10d@4x.png


class DetalleTiempo : AppCompatActivity() {

    lateinit var binding: ActivityDetalleTiempoBinding
    lateinit var recycler: RecyclerView
    lateinit var geo: Geocoder
    lateinit var dia: Dia
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleTiempoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidThreeTen.init(this)
        recycler = findViewById(R.id.recycler_tiempo)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        dia = intent.getParcelableExtra<Dia>(CIUDAD)!!
        geo = Geocoder(this)


        getDias()

    }

    fun getDias(){
        var descriptionPais: MutableList<Address>? = null

        try {
            descriptionPais = geo.getFromLocation(dia.lat,dia.lon,1)
        }catch (e: IOException){
            Toast.makeText(this@DetalleTiempo,"Problemas de conexion!!",Toast.LENGTH_LONG).show()
        }
        if (descriptionPais != null) {
            binding.txtCiudadLdt.text = descriptionPais.get(0).countryName+"\n"+descriptionPais.get(0).locality
        }

        binding.txtTemperaturaLdt.text =""+ Math.round(dia!!.daily?.get(0)?.temp!!.day)
        binding.description.text = dia!!.daily?.get(0)?.weather!![0].description!!.capitalize()
        val recyclerTiempo = RecyclerPrediccionTiempo(dia)

        if (dia != null) {
            Log.i("dia", dia!!.getListHours(dia!!.daily?.get(0)?.dt!!.toInt()).toString())
        }else{
            Log.i("dia","Dia es null")
        }

        recycler.adapter = recyclerTiempo


    }



}