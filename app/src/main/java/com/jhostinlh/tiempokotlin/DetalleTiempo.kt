package com.jhostinlh.tiempokotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleTiempoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycler = findViewById(R.id.recycler_tiempo)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        getiempo()

    }

    fun getiempo() {
        val myApiService: MyApiService? = MyApiAdapter.getApiService()
        val ciudad = intent.getSerializableExtra(CIUDAD).toString()
        val call = myApiService?.getPrediccion7x3(ciudad,"es","metric", API_KEY)

        if (call != null) {
            call.enqueue(object: Callback<PrediccionTiempo> {
                override fun onFailure(call: Call<PrediccionTiempo>?, t: Throwable?) {
                    if (t is IOException) {
                        Log.i("problema","Problemasde conexion!!")
                        // logging probably not necessary
                    } else {
                        Log.i("problema", "problemas de conversion")

                    }
                }

                override fun onResponse(call: Call<PrediccionTiempo>?, response: Response<PrediccionTiempo>?) {
                    if (response != null) {
                        if (!response.isSuccessful){
                            Log.i("response","Sin exito")
                            return
                        }
                        val predTiempo:PrediccionTiempo?= response.body()

                        binding.txtCiudadLdt.text = predTiempo?.city?.name
                        var aux: Double? = predTiempo?.lista?.get(0)?.cmain?.temp
                        var vaux = aux?.let { Math.round(it) }
                        binding.txtTemperaturaLdt.text = vaux.toString()
                        binding.description.text = predTiempo?.lista?.get(0)?.weather?.get(0)?.description
                        val recyclerTiempo = RecyclerPrediccionTiempo(predTiempo?.lista)
                        if (predTiempo != null) {
                            Log.i("lista",predTiempo.lista.toString())
                        }
                        recycler.adapter = recyclerTiempo

                    }else {
                        Log.i("response","Es null")
                    }


                }
            })
        }



    }



}