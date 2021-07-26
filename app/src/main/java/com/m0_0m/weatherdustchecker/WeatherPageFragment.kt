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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

class WeatherPageFragment :Fragment() {

    @JsonIgnoreProperties(ignoreUnknown =  true)
    data class OpenWeatherAPISONResponse(val main: Map<String, String>, val weather: List<Map<String, String>>)

    val appID = ""
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

    @JsonDeserialize(using=PersonDeserializer::class)
    data class Person(var name: String, var age: Int, var address: Address)
    data class Address(var city: String)

    class PersonDeserializer : StdDeserializer<Person>(Person::class.java) {
        override fun deserialize(parser: JsonParser?, ctxt: DeserializationContext?): Person {
            Log.d("mytag", "from deserialize")
            val node = parser?.codec?.readTree<JsonNode>(parser)

            // 전달받은 JSON 객체를 탐색해서 필요한 데이터를 추출
            val name = node?.get("name")?.asText()!!
            val age = node?.get("age")?.asInt()!!
            val address = node?.get("address")
            val city = address?.get("city")?.asText()!!

            return Person(name, age, Address(city))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstState: Bundle?): View {
        val view = inflater.inflate(R.layout.weather_page_fragment, container, false)

        statusText = view.findViewById<TextView>(R.id.weather_status_text)
        temperatureText = view.findViewById<TextView>(R.id.weather_temp_text)
        weatherImage = view.findViewById<ImageView>(R.id.weather_icon)

        return view
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val lat = arguments!!.getDouble("lat")
        val lon = arguments!!.getDouble("lng")
        val url = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=${appID}&lat=${lat}&lon=${lon}"


/*        val data = mapper?.readValue<OpenWeatherAPIJSONResponse>(result)

        val temp = data.main["temp"]


        */

            APICall(object : APICall.APICallback {

                override fun onComplete(result: String) {
                    Log.d("myapp", result)

                    val mapper = jacksonObjectMapper()
                    var data = mapper?.readValue<OpenWeatherAPISONResponse>(result)

                    val temp = data.main.get("temp")

                    temperatureText.text = temp
                    val id = data.weather[0].get("id")
                    if(id != null){
                        statusText.text = when{
                            id.startsWith("2") -> {
                                weatherImage.setImageResource(R.drawable.flash)
                                "천둥, 번개"
                            }
                            id.startsWith("3") -> {
                                weatherImage.setImageResource(R.drawable.rain)
                                "이슬비"
                            }
                            id.startsWith("5") -> {
                                weatherImage.setImageResource(R.drawable.rain)
                                "비"
                            }
                            id.startsWith("6") -> {
                                weatherImage.setImageResource(R.drawable.snow)
                                "눈"
                            }
                            id.startsWith("7") -> {
                                weatherImage.setImageResource(R.drawable.cloudy)
                                "흐림"
                            }
                            id.equals("800") -> {
                                weatherImage.setImageResource(R.drawable.sun)
                                "화창"
                            }
                            id.startsWith("8") -> {
                                    weatherImage.setImageResource(R.drawable.cloud)
                                    "구름 낌"
                            }
                            else -> "알 수 없음"
                        }
                    }
                }
        }).execute(URL(url))


    }
}