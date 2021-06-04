package com.jhostinlh.tiempokotlin.Recycler;

import CadaHora
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jhostinlh.tiempokotlin.R
import com.jhostinlh.tiempokotlin.URL_ICON
import com.jhostinlh.tiempokotlin.databinding.ActivityDetalleBinding
import com.jhostinlh.tiempokotlin.databinding.ItemHoraBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

public class RecyclerDetalleDia constructor(val listaHoras: ArrayList<CadaHora>): RecyclerView.Adapter<RecyclerDetalleDia.Holder>() {
    var lista: ArrayList<CadaHora>
    init {
        lista = listaHoras
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerDetalleDia.Holder {
        val view: View = LayoutInflater.from(parent.context) .inflate(R.layout.item_hora,null,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listaHoras.size
    }

    override fun onBindViewHolder(holder: RecyclerDetalleDia.Holder, position: Int) {
        holder.cargarDatos(lista.get(position))
    }

    //Class Holder
    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var binding: ItemHoraBinding
        init {
            binding = ItemHoraBinding.bind(itemView)
        }
        fun cargarDatos(tiempoHora: CadaHora){

            binding.txtHoraItemhora.text = getdateformatted(tiempoHora.dt.toLong(), "HH:mm")
            binding.txtTempItemHora.text = ""+Math.round(tiempoHora.temp)+ " ºC"

            val url = URL_ICON + tiempoHora.weather.get(0).icon + "@4x.png"
            setIcon(url)
            binding.txtVientoItemHora.text = ""+ Math.round(tiempoHora.wind_speed)+ " m/s"
            binding.txtHumedadItemHora.text = ""+ tiempoHora.humidity+" %"
            binding.txtProbabilidadPrecipitacionItemHora.text = ""+ Math.round(tiempoHora.pop)+" %"
            binding.txtUvItemHora.text =""+ Math.round(tiempoHora.uvi)
        }
        fun getdateformatted(milliseconds: Long, pattern: String): String {

            val instant = Instant.ofEpochSecond(milliseconds)
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            var auxdateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            return date.format(auxdateTimeFormatter)
        }
        private fun setIcon(url: String) {

            Picasso.get().load(url).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    binding.imgIconoTiempoItemHora.setImageBitmap(bitmap)
                }

            })
        }
    }
}
