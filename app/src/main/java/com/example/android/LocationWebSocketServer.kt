package com.example.android

import com.google.gson.Gson
import java.util.*

class LocationWebSocketServer {
    companion object {
        const val LOCATION_TOPIC = "/topic/location"
        const val LOCATION_ENDPOINT = "/api/v1/location"
    }

    fun startServer() {
        println("WebSocket Server started at ws://localhost:8080$LOCATION_ENDPOINT")
    }

    fun sendLocationUpdate(latitude: Double, longitude: Double, altitude: Double) {
        val locationData = mapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "altitude" to altitude,
            "time" to Date().toString()
        )
        println("Server sending: ${Gson().toJson(locationData)}")
    }
}