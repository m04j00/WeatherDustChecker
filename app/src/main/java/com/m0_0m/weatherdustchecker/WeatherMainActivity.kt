package com.m0_0m.weatherdustchecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar


class WeatherMainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_main_activity)

        //getSupportActionBar?.hide()

        val transaction = supportFragmentManager.beginTransaction()

        //transaction.add(R.id.fragment_container, WeatherPageFragment.newInstance(37.39298982923837, 129.22029298574122))
        transaction.add(R.id.fragment_container, DustPageFragment.newInstance(37.39298982923837, 129.22029298574122))

        transaction.commit()

        //val personJSONString = """{ "name": "John", "age": 20, "address": { "city": "Seoul" } }"""

    }
}
