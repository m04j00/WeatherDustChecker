package com.m0_0m.weatherdustchecker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class OperionWeatherAPIResponseGSON(
    val main: Map<String, String>,
    val weather: List<Map<String, String>>
)
interface WeatherAPIService {
    @GET("/data/2.5/weather")
    fun getWeatherStatusInfo(
        @Query("appid") appid: String,
        @Query("lat") lat : Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ) : Call<OperionWeatherAPIResponseGSON>
}

