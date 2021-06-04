package com.jhostinlh.tiempokotlin


import CadaHora
import Diariamente
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jhostinlh.tiempokotlin.Recycler.RecyclerDetalleDia
import com.jhostinlh.tiempokotlin.databinding.ActivityDetalleBinding
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

class Detalle : AppCompatActivity() {
    lateinit var binding: ActivityDetalleBinding
    lateinit var recyclerviewHoras: RecyclerView


    lateinit var intentArrayHoras: ArrayList<CadaHora>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidThreeTen.init(this)

        recyclerviewHoras = findViewById(R.id.recyclerview_horas)
        recyclerviewHoras.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val arraydia = intent.getSerializableExtra("dia") as ArrayList<Diariamente>
        val intentDia = arraydia.get(0)
        intentArrayHoras = intent.getSerializableExtra("listaHoras") as ArrayList<CadaHora>


        var instant = Instant.ofEpochSecond(intentDia?.dt!!.toLong())
        var auxFecha: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        val cadenaFecha: String = auxFecha.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")) +
                " , " + auxFecha.dayOfMonth + " de " + auxFecha.month.getDisplayName(TextStyle.FULL,Locale("es")) + " de " + auxFecha.year
        binding.txtFechaLd.text =cadenaFecha

        val tempMaxmin: String ="" + Math.round(intentDia.temp!!.max) + " / " + Math.round(intentDia.temp!!.min) + " ºC"
        binding.txtTempmaxyminLd.text = tempMaxmin


        binding.txtDescriptionLd.text = intentDia.weather!![0].description!!.capitalize()

        binding.txtHoraSalida1SolLd.text = getdateformatted(intentDia.sunrise!!.toLong(),"HH:mm")

        binding.txtHoraOculta1SolLd.text = getdateformatted(intentDia.sunset!!.toLong(),"HH:mm")

        binding.txtHoraSalidaLunaLd.text = getdateformatted(intentDia.moonrise!!.toLong(),"HH:mm")

        binding.txtHoraOcultaLunaLd.text = getdateformatted(intentDia.moonset!!.toLong(),"HH:mm")

        binding.txtUvaLd.text = "UV "+Math.round(intentDia.uvi!!.toDouble())

        binding.txtProbabilidadprecipitacionLd.text =""+ Math.round(intentDia.pop!!.toDouble())+" %"



        if(intentDia.rain!= null){
            binding.txtVolumenPrecipitacionLd.text =""+ Math.round(intentDia.rain.toDouble())+" mm"
        }else{
            binding.txtVolumenPrecipitacionLd.text = "0 mm"
        }


        val recyclerAdapter = RecyclerDetalleDia(intentArrayHoras)
        recyclerviewHoras.adapter = recyclerAdapter

    }
// Devuelve hora o fecha o los dos formateados a partir de milisegundos tiempo unix y pattern
    fun getdateformatted(milliseconds: Long, pattern: String): String {

        val instant = Instant.ofEpochSecond(milliseconds)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        var auxdateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
        return date.format(auxdateTimeFormatter)
    }


}
