package com.example.android

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.util.*

class LocationWebSocketClient(private val callback: LocationCallback) : WebSocketListener() {
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    companion object {
        private const val TAG = "LocationWebSocketClient"
        const val SERVER_URL = "ws://10.0.2.2/api/v1/location"
    }

    fun connect() {
        val client = OkHttpClient()
        val request = Request.Builder().url(SERVER_URL).build()
        webSocket = client.newWebSocket(request, this)
        Log.d(TAG, "Connecting to WebSocket server...")
    }

    fun sendLocation(latitude: Double, longitude: Double, altitude: Double) {
        val locationData = mapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "altitude" to altitude,
            "time" to Date().toString()
        )
        webSocket?.send(gson.toJson(locationData))
        Log.d(TAG, "Sent location: $locationData")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(TAG, "Received message: $text")
        callback.onLocationReceived(text)
    }


    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, "Connection closed")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.e(TAG, "Connection failed", t)
    }

    interface LocationCallback {
        fun onLocationReceived(message: String)
    }
}