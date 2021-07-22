package com.m0_0m.weatherdustchecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_main_activity)

        //getSupportActionBar?.hide()

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.fragment_container, WeatherPageFragment.newInstance("화창", 10.0))

        transaction.commit()
    }




}