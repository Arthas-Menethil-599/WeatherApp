package com.example.weatherapp.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.models.WeatherData
import com.example.weatherapp.rest.models.Weather
import com.example.weatherapp.rest.services.WeatherService
import com.example.weatherapp.utils.RestUrls
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    companion object {
        val API_KEY = "25f59e8efa0533e28eae4e01e5113b21"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var weatherData: WeatherData? = null
        if (isNetworkAvailable(this)) {
            runBlocking {
                val temporaryData = getCurrentData()
            }
            val weather: WeatherData? = getWeather()
            if(weather != null) {
                binding.address.text = "Nur-Sultan"
                binding.temp.text = (weather.temp - 273.15).toInt().toString() + "°C"
                binding.humidity.text = weather.humidity.toString()
                binding.pressure.text = weather.pressure.toString()
                binding.wind.text = weather.windSpeed.toString()
            }
        } else {
            val weather: WeatherData? = getWeather()
            if (weather == null) {

            } else {
                binding.address.text = "Nur-Sultan"
                binding.temp.text = (weather.temp - 273.15).toInt().toString() + "°C"
                binding.humidity.text = weather.humidity.toString()
                binding.pressure.text = weather.pressure.toString()
                binding.wind.text = weather.windSpeed.toString()
            }
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun saveWeather(weather: com.example.weatherapp.db.models.WeatherData) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "weather.db"
        ).allowMainThreadQueries().build()
        val weatherDao = db.weatherDao()
        runBlocking {
            weatherDao.addWeatherData(weather)
        }
        Log.e("save", "success")

    }

    fun getWeather(): com.example.weatherapp.db.models.WeatherData? {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "weather.db"
        ).allowMainThreadQueries().build()
        val weatherDao = db.weatherDao()
        val weathers: List<com.example.weatherapp.db.models.WeatherData> =
            weatherDao.getWeatherData()
        if (weathers.size - 1 < 0) {
            return null
        } else {
            return weathers[weathers.size - 1]
        }
    }

    suspend fun getCurrentData() : Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl(RestUrls.MAIN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val cityName = "nur-sultan"
        val service = retrofit.create(WeatherService::class.java)
        try {
            val weather = service.getData(cityName, API_KEY)
            var weatherData : WeatherData = WeatherData()
            weatherData = WeatherData(weather.main!!.temp.toDouble(), weather.main!!.pressure.toInt(), weather.main!!.humidity.toInt(), weather.wind!!.speed.toDouble())
            saveWeather(weatherData)
            Log.d("Tag", weather.toString())
            return true
        }
        catch (exception : HttpException) {
            Log.e("MyException", exception.message())
            return false
        }
    }
}


//
//inner class weatherTask() : AsyncTask<String, Void, String>() {
//
//    override fun onPreExecute() {
//        super.onPreExecute()
//        /* Showing the ProgressBar, Making the main design GONE */
//        findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
//        findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
//        findViewById<TextView>(R.id.errorText).visibility = View.GONE
//    }
//
//    override fun doInBackground(vararg params: String?): String? {
//        var response: String?
//        try {
//            response =
//                URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
//                    Charsets.UTF_8
//                )
//        } catch (e: Exception) {
//            response = null
//        }
//        return response
//    }
//
//    override fun onPostExecute(result: String?) {
//        super.onPostExecute(result)
//        try {
//            /* Extracting JSON returns from the API */
//            val jsonObj = JSONObject(result)
//
//            val main = jsonObj.getJSONObject("main")
//            val sys = jsonObj.getJSONObject("sys")
//            val wind = jsonObj.getJSONObject("wind")
//            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
//
//            val updatedAt: Long = jsonObj.getLong("dt")
//            val updatedAtText = "Updated at: " + SimpleDateFormat(
//                "dd/MM/yyyy hh:mm a",
//                Locale.ENGLISH
//            ).format(Date(updatedAt * 1000))
//            val temp = main.getString("temp") + "°C"
//            val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
//            val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
//            val pressure = main.getString("pressure")
//            val humidity = main.getString("humidity")
//
//            val sunrise: Long = sys.getLong("sunrise")
//            val sunset: Long = sys.getLong("sunset")
//            val windSpeed = wind.getString("speed")
//            val weatherDescription = weather.getString("description")
//
//            val address = jsonObj.getString("name") + ", " + sys.getString("country")
//
//            /* Populating extracted data into our views */
//            findViewById<TextView>(R.id.address).text = address
//            findViewById<TextView>(R.id.updated_at).text = updatedAtText
//            findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
//            findViewById<TextView>(R.id.temp).text = temp
//            findViewById<TextView>(R.id.temp_min).text = tempMin
//            findViewById<TextView>(R.id.temp_max).text = tempMax
//            findViewById<TextView>(R.id.sunrise).text =
//                SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
//            findViewById<TextView>(R.id.sunset).text =
//                SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
//            findViewById<TextView>(R.id.wind).text = windSpeed
//            findViewById<TextView>(R.id.pressure).text = pressure
//            findViewById<TextView>(R.id.humidity).text = humidity
//
//            /* Views populated, Hiding the loader, Showing the main design */
//            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
//            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
//
//        } catch (e: Exception) {
//            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
//            findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
//        }
//    }
//}
