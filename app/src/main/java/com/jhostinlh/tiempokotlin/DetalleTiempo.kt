package com.jhostinlh.tiempokotlin

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jhostinlh.tiempokotlin.Modelo.Tiempo
import com.jhostinlh.tiempokotlin.Retrofit.InterfaceRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val CERO_ABS = 273.15
const val BASE_URL = "https://api.openweathermap.org/"
const val API_KEY = "4e719a84e03ef9f6ef6f38418f24e0bb"


class DetalleTiempo : AppCompatActivity() {

    lateinit var txtV_temperatura: TextView
    lateinit var txtV_ciudad: TextView
    lateinit var iconoTiempo: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_tiempo)

        txtV_temperatura = findViewById<TextView>(R.id.txt_temperatura_ldt)
        txtV_ciudad = findViewById<TextView>(R.id.txt_ciudad_ldt)


        iconoTiempo = findViewById<ImageView>(R.id.imgV_tiempo)
        Toast.makeText(this,"El toast sirve!",Toast.LENGTH_LONG).show()
        getTiempo()

    }
    //Devuelve numero aletorio entr -5 y 35 Cº


    //Devuelve la referencia al icono segun la temperatura que haga
    fun getIdIcono(temperatura: Double?): Int {
        var idIcono: Int = 0
        if (temperatura != null) {
            if (temperatura < 5) {
                idIcono = R.drawable.frio
            } else if (temperatura < 15) {
                idIcono = R.drawable.lluvia
            } else {
                idIcono = R.drawable.dom
            }
        }
        return idIcono
    }

    fun getTiempo() {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val interfaceRetro = retrofit.create(InterfaceRetrofit::class.java)
        val ciudad = intent.getSerializableExtra(CIUDAD).toString()
        val call: Call<Tiempo> = interfaceRetro.getTiempo(ciudad, API_KEY)

        call.enqueue(object : Callback<Tiempo> {
            override fun onFailure(call: Call<Tiempo>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
            }
            override fun onResponse(call: Call<Tiempo>?, response: Response<Tiempo>?) {
                if (response != null) {
                    if (response.isSuccessful){
                        var nombreCiudad = "Temperatura en "
                        nombreCiudad += intent.getSerializableExtra(CIUDAD).toString() + ":"
                        txtV_ciudad.text = nombreCiudad
                        val tiempo = response?.body()

                        val temperatura = tiempo?.main?.temp?.minus(CERO_ABS)
                        val aux = temperatura?.let { Math.round(it) };
                        txtV_temperatura.text = aux.toString() + "ºC"

                        iconoTiempo.setImageResource(getIdIcono(temperatura))
                    }else {
                        Toast.makeText(this@DetalleTiempo, "Sin Exito!",Toast.LENGTH_LONG).show()

                    }
                }else{
                    Toast.makeText(this@DetalleTiempo,"es nullo",Toast.LENGTH_LONG).show()
                }

            }

        })

    }
}