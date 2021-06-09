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
import com.jhostinlh.tiempokotlin.databinding.ItemDiaBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

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
        val binding: ItemDiaBinding

        init {
            binding = ItemDiaBinding.bind(itemView)

        }

        fun asignarDatos(dia: Diariamente) {
            val instant: Instant = Instant.ofEpochSecond(dia.dt!!.toLong())

            val time: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            binding.txtDiaItem.text = time.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")).capitalize()

            binding.txtFechaItem.text = getdateformatted(time,"dd/MM/yyyy")

            val url = URL_ICON + dia.weather?.get(0)?.icon + "@4x.png"
            setIcon(url)

            binding.txtTempMaxItem.text =""+ Math.round(dia.temp!!.max)+" ºC"

            binding.txtTempMinItem.text =""+ Math.round(dia.temp.min)+" ºC"

            binding.txtVientoItem.text =""+ Math.round(dia.wind_speed!!.toDouble()) + " m/s"

            binding.txtHumityItem.text = dia.humidity



            if (dia.rain == null) {
                binding.txtPrecipitacionItem.text = "0 mm"
            } else {
                binding.txtPrecipitacionItem.text =""+ Math.round(dia.rain.toDouble()) + " mm"
            }


        }

        private fun setIcon(url: String) {

            Picasso.get().load(url).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    binding.imgIconoTiempoItem.setImageBitmap(bitmap)
                }

            })
        }
        fun getdateformatted(milliseconds: Long, pattern: String): String {

            val instant = Instant.ofEpochSecond(milliseconds)
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            var auxdateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            return date.format(auxdateTimeFormatter)
        }
        fun getdateformatted(date: LocalDateTime, pattern: String): String {
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            return date.format(dateTimeFormatter)
        }
    }
}
