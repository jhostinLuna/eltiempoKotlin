package com.jhostinlh.tiempokotlin

import Dia
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.jhostinlh.tiempokotlin.Retrofit.MyApiAdapter
import com.jhostinlh.tiempokotlin.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        val apiAdapter = MyApiAdapter.getApiService()
        val geo = Geocoder(this)

        val mutableList = geo.getFromLocationName("madrid",1)

        Log.i("geo",mutableList.toString())




    }






    fun intentToDetalle(view: View){
        var txtciudad: String = binding.edittxtCiudad.text.toString()
        val city = getGeocoder(txtciudad)

        if (txtciudad.isNotEmpty() && city.isNotEmpty()){
            var miIntent: Intent = Intent(this,DetalleTiempo::class.java)
                    .apply { putParcelableArrayListExtra(CIUDAD,ArrayList(city)) }
            startActivity(miIntent);
        }else{

            Toast.makeText(this, "¡Introduce una ciudad válida!", Toast.LENGTH_SHORT).show()
        }

    }
    fun getGeocoder(ciudad: String): MutableList<Address>{
        val geocoder = Geocoder(this)
        val city = geocoder.getFromLocationName(ciudad, 1)
        return city
    }
}