package com.example.android

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R

class MP3 : AppCompatActivity() {
    private lateinit var prev : Button
    private lateinit var play: Button
    private lateinit var next: Button
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarSound: SeekBar
    private lateinit var media: Array<MediaPlayer>
    private lateinit var cycle: Button
    lateinit  var runnable: Runnable
    private var track = 0
    private val handler = Handler()
    private  var isCycleMode = false
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
        seekBar = findViewById(R.id.seekbar)
        seekBarSound = findViewById(R.id.seekbarsound)
        cycle = findViewById(R.id.cyclebtn)

        media = arrayOf(
            MediaPlayer.create(this@MP3, R.raw.song1),
            MediaPlayer.create(this@MP3, R.raw.song2)
        )
        media[track].setOnPreparedListener {
            seekBar.max = media[track].duration
        }


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

        cycle.setOnClickListener {
            isCycleMode = !isCycleMode
            cycle.isSelected = isCycleMode
            if (isCycleMode) {
                cycle.text = "Cycle ON"
            } else {
                cycle.text = "Cycle OFF"
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    media[track].seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        runnable = Runnable {
            seekBar.progress = media[track].currentPosition
            handler.postDelayed(runnable, 0)
            media[track].setOnCompletionListener {
                if (isCycleMode) {
                    media[track].seekTo(0)
                    media[track].start()
                } else {
                    media[track].stop()
                    media[track].prepare()
                    track = (track + 1) % media.size
                    media[track].start()
                    seekBar.max = media[track].duration
                }
            }


        }
        handler.postDelayed(runnable, 0)


        seekBarSound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val volume = progress / 100f
                    media[track].setVolume(volume, volume)


                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        seekBarSound.progress = 100
        media[track].setVolume(1f, 1f)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)

        media.forEach { player ->
            player.release()
        }
    }
}