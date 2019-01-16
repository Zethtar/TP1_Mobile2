package ca.csf.mobile2.tp1.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import ca.csf.mobile2.tp1.R

class WeatherActivity : AppCompatActivity(), GetWeatherTask.Listener {

    private lateinit var weatherPreviewImage : ImageView
    private lateinit var temperatureTextView : TextView
    private lateinit var cityTextView : TextView
    private lateinit var progressBar : ProgressBar
    private lateinit var errorImageView : ImageView
    private lateinit var errorTextView : TextView
    private lateinit var retryButton : Button

    private var weather : Weather? = null
    private var isConnectivityError : Boolean = false
    private var isAnError : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        weatherPreviewImage = findViewById(R.id.weatherPreviewImageView)
        temperatureTextView = findViewById(R.id.temperatureTextView)
        cityTextView = findViewById(R.id.cityTextView)
        progressBar = findViewById(R.id.progressBar)
        errorImageView = findViewById(R.id.errorImageView)
        errorTextView = findViewById(R.id.errorTextView)
        retryButton = findViewById(R.id.retryButton)

        if (savedInstanceState == null) {
            startGetWeatherTask()
        }
    }

    override fun onSaveInstanceState(outState: Bundle? ) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(WEATHER_PARCEL, weather)
        outState!!.putBoolean(IS_AN_ERROR_KEY, isAnError)
        outState!!.putBoolean(IS_CONNECTIVITY_ERROR_KEY, isConnectivityError)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        weather = savedInstanceState?.getParcelable(WEATHER_PARCEL)
        isAnError = savedInstanceState!!.getBoolean(IS_AN_ERROR_KEY)
        isConnectivityError = savedInstanceState!!.getBoolean(IS_CONNECTIVITY_ERROR_KEY)

        setVisual()
    }
    private fun startGetWeatherTask() {
        showProgressBar()
        setErrorWidgetsVisibility(View.INVISIBLE)
        setValidWidgetsVisibility(View.INVISIBLE)

        val getWeatherTask = GetWeatherTask()
        getWeatherTask.subscribe(this)
        getWeatherTask.execute()
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setValidWidgetsVisibility(visibility : Int) {
        weatherPreviewImage.visibility = visibility
        temperatureTextView.visibility = visibility
        cityTextView.visibility = visibility
    }

    private fun setErrorWidgetsVisibility(visibility : Int) {
        errorTextView.visibility = visibility
        errorImageView.visibility = visibility
        retryButton.visibility = visibility
    }

    private fun setVisual()
    {
        if(isAnError) {
            setValidWidgetsVisibility(View.INVISIBLE)
            if(isConnectivityError){
                showOperationFailedToConnect()
            }
            else{
                showOperationReceivedJunkFromServer()
            }
        }
        else {
            setErrorWidgetsVisibility(View.INVISIBLE)

            if (weather == null) {
                showProgressBar()
                setErrorWidgetsVisibility(View.INVISIBLE)
                setValidWidgetsVisibility(View.INVISIBLE)
            } else {
                weatherPreviewImage.setImageResource(weather!!.type.imageID)
                cityTextView.text = weather!!.city
                temperatureTextView.text = weather!!.temperatureInCelsius.toString()

                hideProgressBar()

                setValidWidgetsVisibility(View.VISIBLE)
            }
        }
    }

    override fun notifyOperationSuccessful(result: Weather) {
       weather = result

        setVisual()

        isAnError = false
    }

    override fun notifyOperationFailedToConnect() {
        isConnectivityError = true
        isAnError = true

        showOperationFailedToConnect()
    }

    private fun showOperationFailedToConnect(){
        hideProgressBar()

        errorTextView.setText(R.string.error_connectivity)

        setErrorWidgetsVisibility(View.VISIBLE)
    }

    override fun notifyOperationReceivedJunkFromServer() {
        isConnectivityError = false
        isAnError = true

        showOperationReceivedJunkFromServer()
    }

    private fun showOperationReceivedJunkFromServer(){
        hideProgressBar()

        errorTextView.setText(R.string.error_server)

        setErrorWidgetsVisibility(View.VISIBLE)
    }

    fun onRetryButtonClick(androidView : View) {
        startGetWeatherTask()
    }

    companion object {
        private const val WEATHER_PARCEL = "WEATHER"
        private const val IS_AN_ERROR_KEY = "IS_AN_ERROR"
        private const val IS_CONNECTIVITY_ERROR_KEY = "IS_CONNECTIVITY_ERROR"
    }
}