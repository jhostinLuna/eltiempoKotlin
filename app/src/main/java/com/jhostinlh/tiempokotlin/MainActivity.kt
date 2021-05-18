package com.jhostinlh.tiempokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

const val CIUDAD = "com.jhostinlh.tiempokotlin.CIUDAD"
class MainActivity : AppCompatActivity() {
    lateinit var tViewCiudad: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun intentToDetalle(view: View){

        tViewCiudad = findViewById(R.id.edittxt_ciudad)
        var txtciudad: String = tViewCiudad.text.toString()
        if (!txtciudad.isEmpty()){
            var miIntent: Intent = Intent(getApplicationContext(),DetalleTiempo::class.java)
                    .apply { putExtra(CIUDAD,txtciudad) }
            startActivity(miIntent);
        }else{

            Toast.makeText(this, "¡Introduce una ciudad válida!", Toast.LENGTH_SHORT).show()
        }

    }
}