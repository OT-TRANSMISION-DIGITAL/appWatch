package com.example.app_watch.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.app_watch.R
import com.example.app_watch.Singleton
import com.example.app_watch.presentation.Models.Event
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.ChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange

class PusherService : Service() {

    private val channel = "Notificaciones"
    private val channelId = "channelNoti"
    private val notificacionId = 2
    private lateinit var pusher: Pusher

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        println("servicio iniciado")
        val notification = NotificationCompat.Builder(this, channelId).apply {
            setContentTitle("Transmisión Digital")
            setContentText("Tienes nuevas tareas pendientes")
            setSmallIcon(R.drawable.notification)
            priority = NotificationCompat.PRIORITY_HIGH
        }.build()

        // Llamar a startForeground lo antes posible
        startForeground(notificacionId, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        println("estoy en onStartCommand")
        val options = PusherOptions().apply {
            setCluster("mt1")
        }
        pusher = Pusher("235a28e331d4c1247a0d", options)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                println("${change.currentState}")
                if (change.currentState == ConnectionState.DISCONNECTED) {
                    pusher.connect()
                }
            }

            override fun onError(message: String?, code: String?, e: Exception?) {
                println("$message")
            }
        }, ConnectionState.ALL)

        when (Singleton.rol) {
            1 -> {
                val channel = pusher.subscribe("notificaciones_admin")
                channel.bind("notificaciones_admin", object : ChannelEventListener {

                    override fun onSubscriptionSucceeded(channelName: String?) {
                        println("Pusher Suscripción al canal exitosa como admin")
                    }

                    override fun onEvent(event: PusherEvent){
                        println(event.data)
                        val gson = Gson()
                        val evento: Event = gson.fromJson(event.data, Event::class.java)
                        createNotification(evento.message)
                    }
                })
            }
            else -> {
                val channel = pusher.subscribe("notificaciones")
                channel.bind("notificaciones", object : ChannelEventListener {

                    override fun onSubscriptionSucceeded(channelName: String?) {
                        println("Pusher Suscripción al canal exitosa como tecnico")
                    }

                    override fun onEvent(event: PusherEvent){
                        println(event.data)
                        val gson = Gson()
                        val evento: Event = gson.fromJson(event.data, Event::class.java)
                        if(Singleton.user_id == evento.tecnico_id){
                            createNotification(evento.message)
                        }
                    }
                })
            }
        }

        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        pusher.unsubscribe("notificaciones")
        pusher.unsubscribe("notificaciones_admin")
        pusher.disconnect()
    }

    private fun createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId,channel,channelImportance)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(message : String) {
        println("invocando notificacion")

        val notification = NotificationCompat.Builder(this, channelId).also {
            it.setContentTitle("Transmisión Digital")
            it.setContentText(message)
            it.setSmallIcon(R.drawable.notification)
            it.priority = NotificationCompat.PRIORITY_HIGH
        }.build()

        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return;
            }
        }
        notificationManager.notify(notificacionId, notification)
    }

}