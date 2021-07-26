package com.m0_0m.weatherdustchecker

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.lang.Exception

class WeatherDustMainActivity : AppCompatActivity() {
    private lateinit var pager : ViewPager
    private var lat: Double = 37.4665708044902
    private var lon : Double = 126.93287991349258

    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener

    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_dust_main_activity)
        supportActionBar?.hide()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {
                lat = location.latitude
                lon = location.longitude
            }
        }
        pager = findViewById<ViewPager>(R.id.pager)
        val adapter = MyPagerAdapter(supportFragmentManager)
        pager.adapter = adapter

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if(position == 0){
                    Toast.makeText(this@WeatherDustMainActivity,"날시 페이지입니다.", Toast.LENGTH_SHORT).show()
                }else if(position == 1){
                    Toast.makeText(this@WeatherDustMainActivity,"미세먼지 페이지입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {        }

        })
    }
    inner class MyPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm){
        override fun getCount() = 2

        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> WeatherPageFragment.newInstance(lat, lon)
                1 -> DustPageFragment.newInstance(lat, lon)
                else -> throw Exception("페이지가 존재하지 않습니디")
            }

        }
    }
}