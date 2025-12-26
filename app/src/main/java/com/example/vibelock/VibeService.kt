package com.example.vibelock

import android.app.*
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat

class VibeService : Service() {

    private lateinit var vibrator: Vibrator

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        startForeground(1, buildNotification())
        startVibrating()
    }

    private fun startVibrating() {
        val pattern = longArrayOf(0, 1000, 5)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(pattern, 0)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        startForegroundService(
            Intent(applicationContext, VibeService::class.java)
        )
        super.onTaskRemoved(rootIntent)
    }

    private fun buildNotification(): Notification {
        val channelId = "vibelock_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "VibeLock",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Blood Flow Active")
            .setContentText("The heart is still beating.")
            .setOngoing(true)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
