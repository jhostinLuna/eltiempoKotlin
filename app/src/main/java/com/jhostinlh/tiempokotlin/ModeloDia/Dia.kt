import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.io.Serializable

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Dia (

		val lat : Double,
		val lon : Double,
		val timezone : String?,
		val timezone_offset : Int,
		val daily : List<Diariamente>?,
		val hourly : List<CadaHora>?
): Parcelable{
	constructor(parcel: Parcel) : this(
		parcel.readDouble(),
		parcel.readDouble(),
		parcel.readString(),
		parcel.readInt(),
		parcel.createTypedArrayList(Diariamente),
		parcel.createTypedArrayList(CadaHora)
	) {
	}

	// Devuelve una lista del tiempo de cada hora de un dia concreto
	fun getListHours(dt: Int): ArrayList<CadaHora>{
		val instant = Instant.ofEpochSecond(dt.toLong())
		val fechaSelected = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
		var hourlyCopy: ArrayList<CadaHora> = ArrayList()
		if (hourly != null) {
			for (item in hourly){
				val ins = Instant.ofEpochSecond(item.dt.toLong())
				val fecha = LocalDateTime.ofInstant(ins, ZoneId.systemDefault())
				if(fechaSelected.toLocalDate() == fecha.toLocalDate()){
					hourlyCopy.add(item)
				}
			}
		}
		return hourlyCopy
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeDouble(lat)
		parcel.writeDouble(lon)
		parcel.writeString(timezone)
		parcel.writeInt(timezone_offset)
		parcel.writeTypedList(daily)
		parcel.writeTypedList(hourly)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Dia> {
		override fun createFromParcel(parcel: Parcel): Dia {
			return Dia(parcel)
		}

		override fun newArray(size: Int): Array<Dia?> {
			return arrayOfNulls(size)
		}
	}
}