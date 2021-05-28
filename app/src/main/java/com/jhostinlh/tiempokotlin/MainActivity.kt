package com.jhostinlh.tiempokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.jhostinlh.tiempokotlin.Modelo.PrediccionTiempo
import com.jhostinlh.tiempokotlin.Retrofit.MyApiAdapter
import com.jhostinlh.tiempokotlin.Retrofit.MyApiService
import com.jhostinlh.tiempokotlin.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

const val CIUDAD = "com.jhostinlh.tiempokotlin.CIUDAD"
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        prediccionViewModel.prediccion.observe(this, Observer {
            pred = it
        })

         */
    }


    override fun onStart() {
        super.onStart()
    }



    fun intentToDetalle(view: View){

        var txtciudad: String = binding.edittxtCiudad.text.toString()
        if (!txtciudad.isEmpty()){
            var miIntent: Intent = Intent(getApplicationContext(),DetalleTiempo::class.java)
                    .apply { putExtra(CIUDAD,txtciudad) }
            startActivity(miIntent);
        }else{

            Toast.makeText(this, "¡Introduce una ciudad válida!", Toast.LENGTH_SHORT).show()
        }

    }
}