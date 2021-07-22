package com.m0_0m.weatherdustchecker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.net.URL

class WeatherPageFragment :Fragment() {

    lateinit var statusText : TextView
    lateinit var temperatureText : TextView
    lateinit var weatherImage : ImageView
    companion object {
        fun newInstance(lat : Double, lon : Double) : WeatherPageFragment{
            val fragment = WeatherPageFragment()

            val args = Bundle()
            args.putDouble("lat", lat)
            args.putDouble("lon", lon)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstState: Bundle?): View {
        val view = inflater.inflate(R.layout.weather_page_fragment, container, false)

        statusText = view.findViewById<TextView>(R.id.weather_status_text)
        temperatureText = view.findViewById<TextView>(R.id.weather_temp_text)
        weatherImage = view.findViewById<ImageView>(R.id.weather_icon)

        return view
    }

    val appID = ""

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val lat = arguments!!.getDouble("lat")
        val lon = arguments!!.getDouble("lng")
        val url = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=${appID}&lat=${lat}&lon=${lon}"

        APICall(object : APICall.APICallback {
            override fun onComplete(result: String) {
                Log.d("myapp", result)
            }
        }).execute(URL(url))


    }
}