package ca.csf.mobile2.tp1.weather

import android.os.AsyncTask
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

const val WEATHER_URL = "https://m2t1.csfpwmjv.tk/api/v1/weather"

class GetWeatherTask : AsyncTask<Unit, Unit, Weather?>() {

    private val listenersList = mutableListOf<Listener>()
    private var isConnectivityError = false

    override fun doInBackground(vararg params: Unit?): Weather? {
        try {
            val okHttpClient = OkHttpClient()
            val objectMapper = ObjectMapper()

            val request : Request = Request.Builder().url(WEATHER_URL).build()

            val response = okHttpClient.newCall(request).execute()

            return if(response.isSuccessful) {
                val json = response.body()!!.string()
                response.close()

                objectMapper.readValue(json)
            } else {
                isConnectivityError = false
                response.close()
                null
            }
        } catch (e : IOException) {
            isConnectivityError = true
            return null
        }
    }

    override fun onPostExecute(result: Weather?) {
        super.onPostExecute(result)

        if(result != null) {
            for(listener in listenersList) {
                listener.notifyOperationSuccessful(result)
            }
        } else {
            if(isConnectivityError) {
                for(listener in listenersList) {
                    listener.notifyOperationFailedToConnect()
                }
            } else {
                for(listener in listenersList) {
                    listener.notifyOperationReceivedJunkFromServer()
                }
            }

        }
    }

    fun subscribe(listener : Listener) {
        if(!listenersList.contains(listener))
            listenersList.add(listener)
    }

    fun unsubscribe(listener: Listener) {
        if(listenersList.contains(listener))
            listenersList.remove(listener)
    }

    interface Listener {
        fun notifyOperationSuccessful(result : Weather)
        fun notifyOperationFailedToConnect()
        fun notifyOperationReceivedJunkFromServer()
    }
}