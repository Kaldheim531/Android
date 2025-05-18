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
import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File

class MP3 : AppCompatActivity() {
    private lateinit var prev: Button
    private lateinit var play: Button
    private lateinit var next: Button
    private lateinit var seekBar: SeekBar
    private lateinit var seekBarSound: SeekBar
    private lateinit var cycle: Button
    private lateinit var runnable: Runnable
    private var track = 0
    private val handler = Handler()
    private var isCycleMode = false
    private lateinit var mediaPlayer: MediaPlayer
    private val mediaFiles = mutableListOf<Pair<String, Uri>>()
    private var log_tag : String = "MY_LOG_TAG"

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
        mediaPlayer = MediaPlayer()

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                playMusic()
                Toast.makeText(this, "Разрешения получены", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Пожалуйста выдайте разрешение", Toast.LENGTH_LONG).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun playMusic() {
        val musicPath = Environment.getExternalStorageDirectory().path + "/Music"
        val directory = File(musicPath)


        directory.listFiles { file -> file.isFile && file.name.endsWith(".mp3", ignoreCase = true) }?.forEach { mp3File ->
            val title = mp3File.nameWithoutExtension
            val contentUri = Uri.fromFile(mp3File)
            mediaFiles.add(Pair(title, contentUri))
        }

        if (mediaFiles.isNotEmpty()) {
            track = 0
            initializeMediaPlayer()
        }
    }

    private fun initializeMediaPlayer() {

            mediaPlayer.reset()
            mediaPlayer.setDataSource(this, mediaFiles[track].second)
            mediaPlayer.prepare()
            seekBar.max = mediaPlayer.duration

    }

    override fun onResume() {
        super.onResume()

        play.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                play.text = "Pause"
            } else {
                mediaPlayer.pause()
                play.text = "Play"
            }
        }

        next.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.reset()
            track = (track + 1) % mediaFiles.size
            initializeMediaPlayer()
            mediaPlayer.start()
            play.text = "Pause"
        }

        prev.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (track <= 0) {
                track = mediaFiles.size - 1
            } else {
                track = (track - 1) % mediaFiles.size
            }
            initializeMediaPlayer()
            mediaPlayer.start()
            play.text = "Pause"
        }

        cycle.setOnClickListener {
            isCycleMode = !isCycleMode
            cycle.isSelected = isCycleMode
            cycle.text = if (isCycleMode) "Cycle ON" else "Cycle OFF"
            mediaPlayer.isLooping = isCycleMode
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000) // Обновляем каждую секунду
        }
        handler.postDelayed(runnable, 0)

        mediaPlayer.setOnCompletionListener {
            if (!isCycleMode) {
                mediaPlayer.stop()
                mediaPlayer.reset()
                track = (track + 1) % mediaFiles.size
                initializeMediaPlayer()
                mediaPlayer.start()
                seekBar.max = mediaPlayer.duration
            }
        }

        seekBarSound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val volume = progress / 100f
                    mediaPlayer.setVolume(volume, volume)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarSound.progress = 100
        mediaPlayer.setVolume(1f, 1f)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        mediaPlayer.release()
    }
}