package ca.csf.mobile2.tp1.weather

import android.os.Parcel
import android.os.Parcelable

class Weather(val type : WeatherType = WeatherType.CLOUDY,
              val temperatureInCelsius : Int = -99,
              val city : String = "Quebec"): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt() as WeatherType,
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type as Int)
        parcel.writeInt(temperatureInCelsius)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }

}