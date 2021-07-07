import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class CadaHora (

		val dt : Int,
		val temp : Double,
		val feels_like : Double,
		val pressure : Int,
		val humidity : Int,
		val dew_point : Double,
		val uvi : Double,
		val clouds : Int,
		val visibility : Int,
		val wind_speed : Double,
		val wind_deg : Int,
		val wind_gust : Double,
		val weather : List<TiempoDia>,
		val pop : Double,
		val rain : Lluvia?
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readInt(),
			parcel.readDouble(),
			parcel.readDouble(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readDouble(),
			parcel.readDouble(),
			parcel.readInt(),
			parcel.readInt(),
			parcel.readDouble(),
			parcel.readInt(),
			parcel.readDouble(),
			parcel.createTypedArrayList(TiempoDia)!!,
			parcel.readDouble(),
			parcel.readParcelable(Lluvia::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(dt)
		parcel.writeDouble(temp)
		parcel.writeDouble(feels_like)
		parcel.writeInt(pressure)
		parcel.writeInt(humidity)
		parcel.writeDouble(dew_point)
		parcel.writeDouble(uvi)
		parcel.writeInt(clouds)
		parcel.writeInt(visibility)
		parcel.writeDouble(wind_speed)
		parcel.writeInt(wind_deg)
		parcel.writeDouble(wind_gust)
		parcel.writeTypedList(weather)
		parcel.writeDouble(pop)
		parcel.writeParcelable(rain, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<CadaHora> {
		override fun createFromParcel(parcel: Parcel): CadaHora {
			return CadaHora(parcel)
		}

		override fun newArray(size: Int): Array<CadaHora?> {
			return arrayOfNulls(size)
		}
	}
}