package com.example.android

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.json.JSONObject
import java.io.File
import org.json.JSONArray
import android.location.Location

class Location : AppCompatActivity(), LocationWebSocketClient.LocationCallback {
    private lateinit var locationCallback: LocationCallback

    private lateinit var webSocketClient: LocationWebSocketClient
    private lateinit var webSocketServer: LocationWebSocketServer

    val value: Int = 0
    val LOG_TAG: String = "LOCATION_ACTIVITY"
    private lateinit var bBackToMain: Button

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private lateinit var myFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvLat: TextView
    private lateinit var tvLon: TextView
    private lateinit var tvAlt: TextView
    private lateinit var tvTime: TextView

    override fun onLocationReceived(message: String) {
        runOnUiThread {
            Toast.makeText(this, "Received: $message", Toast.LENGTH_SHORT).show()

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_location)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bBackToMain = findViewById<Button>(R.id.back_to_main)

        myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        tvLat = findViewById(R.id.tv_lat) as TextView
        tvLon = findViewById(R.id.tv_lon) as TextView
        tvAlt = findViewById(R.id.tv_at) as TextView
        tvTime = findViewById(R.id.tv_curtime) as TextView

        webSocketClient = LocationWebSocketClient(this)
        webSocketServer = LocationWebSocketServer()
        webSocketServer.startServer()
        webSocketClient.connect()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    tvLat.text = "Latitude: ${location.latitude}"
                    tvLon.text = "Longitude: ${location.longitude}"
                    tvAlt.text = "Altitude: ${location.altitude} meters"
                    val time = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault()).format(Date(location.time))
                    tvTime.text = "Last update: $time"

                }
            }
        }



    }

    override fun onResume() {
        super.onResume()

        bBackToMain.setOnClickListener({
            val backToMain = Intent(this, MainActivity::class.java)
            startActivity(backToMain)
        })

        getCurrentLocation()

    }


    private fun getCurrentLocation(){

        if(checkPermissions()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }

                val locationRequest = LocationRequest.create().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 30000
                    fastestInterval = 30000
                }

                myFusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                myFusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task->
                    val location: Location?=task.result
                    if(location == null){
                        Toast.makeText(applicationContext, "problems with signal", Toast.LENGTH_SHORT).show()
                    } else {
                        tvLat.text = "Latitude: ${location.latitude}"
                        tvLon.text = "Longitude: ${location.longitude}"
                        tvAlt.text = "Altitude: ${location.altitude} meters"
                        val time = java.text.SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(java.util.Date(location.time))
                        tvTime.text = "Last update: $time"

                        saveCurrentLocation(location.latitude, location.longitude, location.altitude)
                    }
                }

            } else{
                // open settings to enable location
                Toast.makeText(applicationContext, "Enable location in settings", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            Log.w(LOG_TAG, "location permission is not allowed");
            tvLat.setText("Permission is not granted")
            tvLon.setText("Permission is not granted")
            requestPermissions()
        }

    }

    private fun saveCurrentLocation(latitude: Double, longitude: Double, altitude: Double) {
        val timeFormat = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        val newEntry = JSONObject().apply {
            put("time", currentTime)
            put("latitude", latitude)
            put("longitude", longitude)
            put("altitude", altitude)
        }

        val externalDir = getExternalFilesDir(null)
        val file = File(externalDir, "current_location.json")

        val jsonArray = if (file.exists()) JSONArray(file.readText()) else JSONArray()

        jsonArray.put(newEntry)

        file.writeText(jsonArray.toString(4))

        Toast.makeText(this, "Saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        webSocketClient.sendLocation(latitude, longitude, altitude)
    }


    private fun requestPermissions() {
        Log.w(LOG_TAG, "requestPermissions()");
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermissions(): Boolean{
        if( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            return true
        } else {
            return false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Denied by user", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun isLocationEnabled(): Boolean{
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }


}