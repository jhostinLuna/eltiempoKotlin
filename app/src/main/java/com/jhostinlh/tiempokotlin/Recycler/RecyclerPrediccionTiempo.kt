package com.jhostinlh.  tiempokotlin.Recycler

import CadaHora
import Dia
import Diariamente
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jhostinlh.tiempokotlin.Detalle
import com.jhostinlh.tiempokotlin.R
import com.jhostinlh.tiempokotlin.URL_ICON
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class RecyclerPrediccionTiempo constructor(val dia: Dia?): RecyclerView.Adapter<RecyclerPrediccionTiempo.Holder>() {
    lateinit var listaDias: List<Diariamente>

    init {
        if (dia != null) {
            listaDias = dia.daily
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): Holder {

        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dia, null, false)

        return Holder(view)
    }


    override fun getItemCount(): Int {
        if (listaDias != null) {
            return listaDias.size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        if (listaDias != null) {
            holder.asignarDatos(listaDias.get(position))
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(holder.itemView.context, Detalle::class.java)
                    //cojo los milisegundos del dia para pasarlos a LocalDateTime
                    val dt = listaDias.get(position).dt
                    val arrayHoras: ArrayList<CadaHora> = dia!!.getListHours(dt!!.toInt())
                    val datosDia: Diariamente? = listaDias.get(position)
                    val arraydia = ArrayList<Diariamente>()
                    if (datosDia != null) {
                        arraydia.add(datosDia)
                    }
                    //getListHours funcion de Clase dia que me devuelve la lista de Horas de ese dia
                    intent.putExtra("listaHoras", arrayHoras)
                    intent.putExtra("dia",arraydia )
                    holder.itemView.context.startActivity(intent)
                }

            })
        } else {
            Log.i("pruebanull", "ES null")
        }
    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var urlImg = "https://openweathermap.org/img/wn/" //10d@4x.png

        var dia: TextView? = null
        var fecha: TextView? = null
        var temp_max: TextView? = null
        var temp_min: TextView? = null
        var precipitacion: TextView? = null
        var humedad: TextView? = null
        var viento: TextView? = null
        var hora: TextView? = null
        var iconoTiempo: ImageView? = null
        var ciudad: TextView? = null

        init {
            dia = itemView.findViewById(R.id.txt_dia_item)
            fecha = itemView.findViewById(R.id.txt_uv_itemHora)
            temp_max = itemView.findViewById(R.id.txt_hora_itemhora)
            temp_min = itemView.findViewById(R.id.txt_temp_itemHora)
            precipitacion = itemView.findViewById(R.id.txt_precipitacion_item)
            humedad = itemView.findViewById(R.id.txt_humity_item)
            viento = itemView.findViewById(R.id.txt_viento_item)
            iconoTiempo = itemView.findViewById(R.id.img_icono_tiempo_itemHora)
            hora = itemView.findViewById(R.id.txt_hora_item)
            ciudad = itemView.findViewById(R.id.txt_ciudad_ldt)
        }

        fun asignarDatos(dia: Diariamente) {
            val instant: Instant = Instant.ofEpochSecond(dia.dt!!.toLong())

            val time: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            this.dia?.text = time.dayOfWeek.toString()

            val datatimeformater = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val vfecha = time.format(datatimeformater)
            fecha?.text = vfecha

            val url = URL_ICON + dia.weather?.get(0)?.icon + "@4x.png"
            setIcon(url)

            temp_max?.text = Math.round(dia.temp!!.max).toString()

            temp_min?.text = Math.round(dia.temp.min).toString()

            viento?.text = Math.round(dia.wind_speed!!.toDouble()).toString() + " Km/h"

            humedad?.text = dia.humidity


            precipitacion?.text
            if (dia.rain == null) {
                precipitacion?.text = "0 mm"
            } else {
                Math.round(dia.rain.toDouble()).toString() + "0 mm"
            }


        }

        private fun setIcon(url: String) {

            Picasso.get().load(url).into(object : Target {
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
}
