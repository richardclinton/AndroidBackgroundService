package com.beanworth.appservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "order"
    private val CHANNEL_NAME = "ORDER"
    lateinit var button:Button
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        button = findViewById(R.id.button)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this,MyService::class.java)
        button.setOnClickListener {
            startService(intent)
        }
    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
           val serviceChannel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_LOW)
            val notificationManager = ContextCompat.getSystemService(this,NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }
}