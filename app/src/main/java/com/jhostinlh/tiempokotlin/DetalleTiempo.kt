package com.jhostinlh.tiempokotlin

import Dia
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jhostinlh.tiempokotlin.Modelo.PrediccionTiempo
import com.jhostinlh.tiempokotlin.Recycler.RecyclerPrediccionTiempo
import com.jhostinlh.tiempokotlin.Retrofit.MyApiAdapter
import com.jhostinlh.tiempokotlin.Retrofit.MyApiService
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
    lateinit var arrayCoord: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleTiempoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidThreeTen.init(this)
        recycler = findViewById(R.id.recycler_tiempo)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        arrayCoord = intent.getSerializableExtra(CIUDAD).toString().split(";")
        geo = Geocoder(this)
        Log.i("array",arrayCoord[0]+"--"+arrayCoord[1])

        getDias()

    }

    fun getDias(){
        val geocoder: MutableList<Address> = geo.getFromLocation(arrayCoord[0].toDouble(),arrayCoord[1].toDouble(),1)
        val latitude: String = arrayCoord[0]
        val longitude: String = arrayCoord[1]
        val apiAdapter = MyApiAdapter.getApiService()
        val call = apiAdapter?.getPrediccionDias(
                latitude, longitude,
                "current,minutely,alerts","metric","es", API_KEY)

        if (call != null) {
            call.enqueue(object :Callback<Dia> {
                override fun onResponse(call: Call<Dia>, response: Response<Dia>) {
                    if (!response.isSuccessful){
                        Log.i("response", "Response sin exito!!")
                        return
                    }
                    val respuesta = response.body()
                    if (respuesta != null) {
                        Log.i("responseDia",respuesta.toString())

                        binding.txtCiudadLdt.text = geocoder.get(0).locality
                        binding.txtTemperaturaLdt.text =""+ Math.round(respuesta.daily.get(0).temp!!.day)
                        binding.description.text = respuesta.daily.get(0).weather!!.get(0).description!!.capitalize()
                        val recyclerTiempo = RecyclerPrediccionTiempo(respuesta)

                        if (respuesta != null) {
                            Log.i("dia", respuesta.getListHours(respuesta.daily.get(0).dt!!.toInt()).toString())
                        }else{
                            Log.i("dia","Dia es null")
                        }

                        recycler.adapter = recyclerTiempo


                    }
                }

                override fun onFailure(call: Call<Dia>, t: Throwable) {
                    if (t is IOException) {
                        Log.i("problema","Problemasde conexion!!")
                        // logging probably not necessary
                    } else {
                        Log.i("problema", "problemas de conversion")

                    }
                }
            } )
        }

    }



}