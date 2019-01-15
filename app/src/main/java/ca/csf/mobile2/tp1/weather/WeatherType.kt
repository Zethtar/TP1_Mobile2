package ca.csf.mobile2.tp1.weather

import ca.csf.mobile2.tp1.R

enum class WeatherType(val imageID : Int) {
    SUNNY(R.drawable.ic_sunny),
    PARTLY_SUNNY(R.drawable.ic_partly_sunny),
    CLOUDY(R.drawable.ic_cloudy),
    RAIN(R.drawable.ic_rain),
    SNOW(R.drawable.ic_snow)
}