package com.example.android

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R

class MP3 : AppCompatActivity() {
    private lateinit var prev : Button
    private lateinit var play: Button
    private lateinit var next: Button
    private lateinit var media: Array<MediaPlayer>
    private var track = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mp3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        prev = findViewById(R.id.prevbtn)
        play = findViewById(R.id.playbtn)
        next = findViewById(R.id.nextbtn)
        media = arrayOf(
            MediaPlayer.create(this@MP3, R.raw.)
            MediaPlayer.create(this@MP3, R.raw.)
        )
    }

    override fun onResume() {
        super.onResume()
        play.setOnClickListener{
            if(!media[track].isPlaying){
                media[track].start()
            }else{
                media[track].pause()
            }
        }
        next.setOnClickListener{
            media[track].stop()
            media[track].prepare()

            track = (track + 1) % media.size
            media[track].start()
        }

        prev.setOnClickListener{
            media[track].stop()
            media[track].prepare()

            if(track <= 0){
                track = media.size - 1
            }else{
                track = (track - 1) % media.size
            }
        }
    }
}