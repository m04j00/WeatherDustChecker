package com.m0_0m.weatherdustchecker

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.lang.Exception

class WeatherDustMainActivity : AppCompatActivity() {
    private lateinit var pager : ViewPager
    private var lat: Double = 37.39287945527131
    private var lon : Double = 129.22019748428113

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

                locationManager.removeUpdates(this)

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
                    Toast.makeText(this@WeatherDustMainActivity,"날씨 페이지입니다.", Toast.LENGTH_SHORT).show()
                }else if(position == 1){
                    Toast.makeText(this@WeatherDustMainActivity,"미세먼지 페이지입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {        }

        })

        if(
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
        ){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    locationListener
            )
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingSuperCall", "MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            var allPermissionGranted = true
            for(result in grantResults){
                allPermissionGranted = (result == PackageManager.PERMISSION_GRANTED)
                if(!allPermissionGranted) break
            }
            if(allPermissionGranted){
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0f,
                        locationListener
                )
            } else{
                Toast.makeText(this, "위치 정보 제공 동의가 필요합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            Log.d("mytag", "${lat}, ${lon}")
        }
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