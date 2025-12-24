package com.example.myapplication
import com.example.android.Calculator
import com.example.android.MP3
import com.example.android.Location
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    private lateinit var calculator: Button
    private lateinit var mp3: Button
    private lateinit var location: Button
    private lateinit var zmqclient: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        calculator = findViewById(R.id.openCalculator)
        calculator.setOnClickListener {
            val calcIntent  = Intent(this, Calculator::class.java)
            startActivity(calcIntent )
        }

        mp3 = findViewById(R.id.openMP3)
        mp3.setOnClickListener{
            val  mp3Intent  = Intent(this, MP3::class.java)
            startActivity(mp3Intent )
        }

        location = findViewById(R.id.openLocation)
        location.setOnClickListener {
            val locIntent  = Intent(this, Location::class.java)
            startActivity(locIntent )
        }


    }
}