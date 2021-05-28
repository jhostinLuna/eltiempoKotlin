package com.jhostinlh.tiempokotlin.Recycler

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jhostinlh.tiempokotlin.Modelo.Lista
import com.jhostinlh.tiempokotlin.R
import com.jhostinlh.tiempokotlin.URL_ICON
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class RecyclerPrediccionTiempo constructor(val listaDias: ArrayList<Lista>?): RecyclerView.Adapter<RecyclerPrediccionTiempo.Holder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerPrediccionTiempo.Holder {
        /*
        val itemBinding =ItemDiaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemBinding)

         */
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dia, null, false)
        return Holder(view)
    }



    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerPrediccionTiempo.Holder, position: Int) {

        if (listaDias != null) {
            holder.asignarDatos(listaDias.get(position))
        }else{
            Log.i("pruebanull","ES null")
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var urlImg = "https://openweathermap.org/img/wn/" //10d@4x.png

        var dia: TextView? = null
        var fecha:TextView? = null
        var temp_max:TextView? = null
        var temp_min:TextView? = null
        var precipitacion:TextView? = null
        var humedad:TextView? = null
        var viento:TextView? = null
        var hora:TextView? = null
        var iconoTiempo: ImageView? = null
        var ciudad: TextView? = null
        init {
            dia = itemView.findViewById(R.id.txt_dia_item)
            fecha = itemView.findViewById(R.id.txt_fecha_item)
            temp_max = itemView.findViewById(R.id.txt_tempmax_item)
            temp_min = itemView.findViewById(R.id.txt_tempmin)
            precipitacion = itemView.findViewById(R.id.txt_precipitacion_item)
            humedad = itemView.findViewById(R.id.txt_humity_item)
            viento = itemView.findViewById(R.id.txt_viento_item)
            iconoTiempo = itemView.findViewById(R.id.img_dia_item)
            hora = itemView.findViewById(R.id.txt_hora_item)
            ciudad = itemView.findViewById(R.id.txt_ciudad_ldt)
        }
        fun asignarDatos(lista: Lista) {
            val instant:Instant =Instant.ofEpochSecond(lista.dt!!.toLong())

            val time: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            dia?.text = time.dayOfWeek.toString()

            val datatimeformater = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val vfecha = time.format(datatimeformater)
            fecha?.text = vfecha

            val timeformater = DateTimeFormatter.ofPattern("HH:mm")
            val vhora = time.format(timeformater)
            hora?.text = vhora

            val url = URL_ICON + lista.weather?.get(0)?.icon  + "@4x.png"
            setIcon(url)
            var aux: Double? = lista.cmain?.tempMax
            var vaux = aux?.let { Math.round(it) }
            temp_max?.text = vaux.toString()+"º"
            aux = lista.cmain?.tempMin
            vaux = aux?.let { Math.round(it) }
            temp_min?.text = vaux.toString()+"º"
            aux = lista.wind?.speed
            vaux = aux?.let { Math.round(it) }
            viento?.text = vaux.toString()+"km/h"

            humedad?.text = lista.cmain?.humidity.toString()+"%"

            precipitacion?.text = lista.cmain?.pressure.toString()+"kph"

        }
        private fun setIcon(url: String){

            Picasso.get().load(url).into(object : Target{
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    iconoTiempo?.setImageBitmap(bitmap)
                }

            })
        }
    }

/*
    class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {





    }


 */
}