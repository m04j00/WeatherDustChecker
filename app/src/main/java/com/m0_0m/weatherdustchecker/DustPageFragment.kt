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
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URL

@Suppress("DEPRECATION")
class DustPageFragment : Fragment() {
    private val APP_TOKEN = ""

    lateinit var statusImage : ImageView
    lateinit var pm25StatusText : TextView
    lateinit var pm25IntensivyText : TextView
    lateinit var pm10StatusText : TextView
    lateinit var pm10IntensivyText : TextView

    companion object {
        fun newInstance(lat : Double, lon : Double) : DustPageFragment {
            val fragment = DustPageFragment()

            val args = Bundle()
            args.putDouble("lat", lat)
            args.putDouble("lon", lon)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dust_page_fragment, container, false)

        statusImage = view.findViewById<ImageView>(R.id.dust_status_icon)
        pm25StatusText = view.findViewById<TextView>(R.id.dust_pm25_status_text)
        pm25IntensivyText = view.findViewById<TextView>(R.id.dust_pm25_intensity_text)
        pm10StatusText = view.findViewById<TextView>(R.id.dust_pm10_status_text)
        pm10IntensivyText = view.findViewById<TextView>(R.id.dust_pm10_intensity_text)

        return view
    }

    @JsonDeserialize(using=DustCheckerResponseDeserializer::class)
    data class DustCheckResponse(val pm10: Int?, val pm25: Int?, val pm10Status: String, val pm25status: String)

    class DustCheckerResponseDeserializer : StdDeserializer<DustCheckResponse>(DustCheckResponse::class.java){
/*        private fun checkCategory(aqi: Int?) : String {
            // null 이면 "알 수 없음"
            // 0 ~ 100 사이 값이면 "좋음"
            // 101 ~ 200 사이 값이면 "보통"
            // 201 ~ 300 사이 값이면 "나쁨"
            // 그 와에는 "매우 나쁨" 문자열을 반환
            // 범위 객체와 in 연산자를 이용하여 포함관계 조사하도록 짜보기 (ex: 50 in 0 .. 100)
            if(aqi == null) {
                return "알 수 없음"
            } else {
                if(aqi in 0 .. 100)     return "좋음"
                if(aqi in 101 .. 200)   return "보통"
                if(aqi in 201 .. 300)   return "나쁨"
                return "매우 나쁨"
            }
        }*/

        private val checkCategory = {
            aqi : Int? -> when(aqi){
                null -> "알 수 없음"
                in 0 .. 100 -> "좋음"
                in 101 .. 200 -> "보통"
                in 201 .. 300 -> "나쁨"
                else -> "매우 나쁨"
            }
        }

        override fun deserialize(parser: JsonParser?, ctxt: DeserializationContext?): DustCheckResponse {
            val node : JsonNode? = parser?.codec?.readTree<JsonNode>(parser)

            // 여기서부터 내부 데이터 추출 코드 작성
            var datanode = node?.get("data")
            var iapinode = datanode?.get("iapi")
            var pm10node = iapinode?.get("pm10")
            var pm25node = iapinode?.get("pm25")
            var pm10v = pm10node?.get("v")?.asInt()
            var pm25v = pm25node?.get("v")?.asInt()

            // 추출한 데이터 가지고 객체 생성해서 반환
            return DustCheckResponse(pm10v, pm25v, checkCategory(pm10v), checkCategory(pm25v))

        }

    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lat = arguments!!.getDouble("lat")
        val lon = arguments!!.getDouble("lon")
        val url = "http://api.waqi.info/feed/geo:${lat};${lon}/?token=${APP_TOKEN}"

        APICall(object : APICall.APICallback {
            override fun onComplete(result: String) {
                Log.d("myapp", result)

                val mapper = jacksonObjectMapper()
                val data = mapper?.readValue<DustCheckResponse>(result)

                if(data != null) {
                    // (1)
                    statusImage.setImageResource(when (data.pm25status) {
                        "좋음" -> R.drawable.good
                        "보통" -> R.drawable.normal
                        "나쁨" -> R.drawable.bad
                        else -> R.drawable.very_bad
                    })
                }

                pm25IntensivyText.text = data.pm25?.toString() ?: "알 수 없음"
                pm25StatusText.text = "${data.pm25status} (초미세먼지)"
                pm10IntensivyText.text = data.pm10?.toString() ?: "알 수 없음"
                pm10StatusText.text = "${data.pm10Status} (미세먼지)"
            }
        }).execute(URL(url))
    }
}