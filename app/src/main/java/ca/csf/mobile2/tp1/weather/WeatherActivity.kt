package ca.csf.mobile2.tp1.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import ca.csf.mobile2.tp1.R

//TODO's
//-Land display is not implemented yet (need to add onRestoreInstance / onSaveInstance)
//-Possibly make Weather a parcelable - To easily save it as parcelable
//-Find a better way to sort what type of error in GetWeatherTask
class WeatherActivity : AppCompatActivity(), GetWeatherTask.Listener {

    private lateinit var weatherPreviewImage : ImageView
    private lateinit var temperatureTextView : TextView
    private lateinit var cityTextView : TextView
    private lateinit var progressBar : ProgressBar
    private lateinit var errorImageView : ImageView
    private lateinit var errorTextView : TextView
    private lateinit var retryButton : Button

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

        startGetWeatherTask()
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

    override fun notifyOperationSuccessful(result: Weather) {
        weatherPreviewImage.setImageResource(result.type.imageID)
        cityTextView.text = result.city
        temperatureTextView.text = result.temperatureInCelsius.toString()

        hideProgressBar()

        setValidWidgetsVisibility(View.VISIBLE)
    }

    override fun notifyOperationFailedToConnect() {
        hideProgressBar()

        errorTextView.setText(R.string.error_connectivity)

        setErrorWidgetsVisibility(View.VISIBLE)
    }

    override fun notifyOperationReceivedJunkFromServer() {
        hideProgressBar()

        errorTextView.setText(R.string.error_server)

        setErrorWidgetsVisibility(View.VISIBLE)
    }

    fun onRetryButtonClick(androidView : View) {
        startGetWeatherTask()
    }
}