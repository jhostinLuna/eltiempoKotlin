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


data class Diariamente (

		val dt : String?,
		val sunrise : String?,
		val sunset : String?,
		val moonrise : String?,
		val moonset : String?,
		val moon_phase : String?,
		val temp : Temperatura?,
		val feels_like : SensacionTermica?,
		val pressure : String?,
		val humidity : String?,
		val dew_point : String?,
		val wind_speed : String?,
		val wind_deg : String?,
		val wind_gust : String?,
		val weather : List<TiempoDia>?,
		val clouds : String?,
		val pop : String?,
		val rain : String?,
		val uvi : String?
): Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readParcelable(Temperatura::class.java.classLoader),
			parcel.readParcelable(SensacionTermica::class.java.classLoader),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.createTypedArrayList(TiempoDia),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(dt)
		parcel.writeString(sunrise)
		parcel.writeString(sunset)
		parcel.writeString(moonrise)
		parcel.writeString(moonset)
		parcel.writeString(moon_phase)
		parcel.writeParcelable(temp, flags)
		parcel.writeParcelable(feels_like, flags)
		parcel.writeString(pressure)
		parcel.writeString(humidity)
		parcel.writeString(dew_point)
		parcel.writeString(wind_speed)
		parcel.writeString(wind_deg)
		parcel.writeString(wind_gust)
		parcel.writeTypedList(weather)
		parcel.writeString(clouds)
		parcel.writeString(pop)
		parcel.writeString(rain)
		parcel.writeString(uvi)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Diariamente> {
		override fun createFromParcel(parcel: Parcel): Diariamente {
			return Diariamente(parcel)
		}

		override fun newArray(size: Int): Array<Diariamente?> {
			return arrayOfNulls(size)
		}
	}
}