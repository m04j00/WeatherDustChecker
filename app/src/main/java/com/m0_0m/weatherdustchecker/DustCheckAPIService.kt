package com.m0_0m.weatherdustchecker

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Type

interface DustCheckAPIService {
    @GET("/feed/geo:{lat};{lon}/")
    fun getDustStatusInfo(
        @Path("lat") lat : Double,
        @Path("lon") lon: Double,
        @Query("token") token: String
    ) : Call<DustCheckResponseFromGSON>
}

data class DustCheckResponseFromGSON(val pm10: Int?, val pm25: Int?, val pm10Status: String, val pm25status: String,
                                     val co: Double?, val o3: Double?, val no2: Double?)

class DustCheckerResponseDeserializerGSON : JsonDeserializer<DustCheckResponseFromGSON> {

    private val checkCategory = { aqi : Int -> when(aqi) {
        in (0 .. 100) -> "좋음"
        in (101 .. 200) -> "보통"
        in (201 .. 300) -> "나쁨"
        else -> "매우 나쁨"
    }}

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DustCheckResponseFromGSON {
        val root = json?.asJsonObject

        val data = root?.getAsJsonObject("data")
        val iaqi = data?.getAsJsonObject("iaqi")
        val pm10 = iaqi?.getAsJsonObject("pm10")
        val pm25 = iaqi?.getAsJsonObject("pm25")
        val co = iaqi?.getAsJsonObject("co")
        val o3 = iaqi?.getAsJsonObject("o3")
        val no2 = iaqi?.getAsJsonObject("no2")

        val pm10v = pm10?.get("v")?.asInt
        val pm25v = pm25?.get("v")?.asInt
        val cov = co?.get("v")?.asDouble
        val o3v = o3?.get("v")?.asDouble
        val no2v = no2?.get("v")?.asDouble

        return DustCheckResponseFromGSON(pm10v, pm25v, checkCategory(pm10v!!), checkCategory(pm25v!!), cov, o3v, no2v)
    }

}