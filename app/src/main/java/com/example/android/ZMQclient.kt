package com.example.android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

class ZMQclient : AppCompatActivity() {

    private lateinit var tvSockets: TextView
    private val serverIp = "192.168.31.211" // Ваш IP
    private val serverPort = 8888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zmqclient)
        tvSockets = findViewById(R.id.tvSockets)

        // Запускаем клиент
        Thread { startClient() }.start()
    }

    private fun startClient() {
        val context = ZContext()
        val socket = context.createSocket(SocketType.REQ)

        try {
            socket.connect("tcp://$serverIp:$serverPort")
            runOnUiThread { tvSockets.append(" Подключено к серверу\n\n") }

            for (i in 1..10) {
                val message = "Hello from Android! #$i"

                socket.send(message.toByteArray(ZMQ.CHARSET), 0)
                runOnUiThread { tvSockets.append("Отправлено: $message\n") }

                val reply = socket.recv(0)
                val replyText = String(reply, ZMQ.CHARSET)
                runOnUiThread { tvSockets.append("Ответ: $replyText\n\n") }

                Thread.sleep(1000)
            }

        } catch (e: Exception) {
            runOnUiThread { tvSockets.append(" Ошибка: ${e.message}\n") }
        } finally {
            socket.close()
            context.close()
        }
    }
}