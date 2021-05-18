package com.jhostinlh.tiempokotlin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class DetalleTiempo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_tiempo)

        val txtV_temperatura = findViewById<TextView>(R.id.txt_temperatura_ldt)
        val txtV_ciudad = findViewById<TextView>(R.id.txt_ciudad_ldt)

        var nombreCiudad = "Temperatura en "
        nombreCiudad += intent.getSerializableExtra(CIUDAD).toString() + ":"
        txtV_ciudad.text = nombreCiudad
        val temperatura: Int = getNumAleatorio()
        txtV_temperatura.text = temperatura.toString() + "ºC"
        val iconoTiempo = findViewById<ImageView>(R.id.imgV_tiempo)


        iconoTiempo.setImageResource(getIdIcono(temperatura))
    }
    //Devuelve numero aletorio entr -5 y 35 Cº
    fun getNumAleatorio(): Int {
        //Random r = new Random();
        //int aux = r.nextInt(3);
        return Math.floor(Math.random() * (35 - -5 + 1) + -5).toInt()
    }

    //Devuelve la referencia al icono segun la temperatura que haga
    fun getIdIcono(temperatura: Int): Int {
        val idIcono: Int
        if (temperatura < 5) {
            idIcono = R.drawable.frio
        } else if (temperatura < 15) {
            idIcono = R.drawable.lluvia
        } else {
            idIcono = R.drawable.dom
        }
        return idIcono
    }
}