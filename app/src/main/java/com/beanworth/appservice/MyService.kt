package com.beanworth.appservice

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyService : Service() {
 private val TAG = "MyService"
    var QUE_NAME = ""
    private val CHANNEL_ID = "order"
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG,"Service Started")
        coroutineScope.launch(Dispatchers.IO){
            consumeNotification()
        }
        val message = intent?.getStringExtra("message")
        startForeground(1,notification(""))
        return START_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    fun notification(message:String?):Notification{
       val notification = NotificationCompat.Builder(this,CHANNEL_ID)
           .setContentTitle("VUUP VENDOR")
           .setContentText(message)
           .setSmallIcon(R.drawable.egg_icon)
           .build()
        return notification
    }

    fun consumeNotification(){
        val factory = ConnectionFactory()
        try {
            factory.host = "35.236.254.219"
            factory.port = 5672
            val connection = factory.newConnection()
            val channel = connection.createChannel()
            channel.queueDeclare(QUE_NAME, false, false, false, null)
            Log.i(TAG,"Waiting for an Event")

            val consumer = object: DefaultConsumer(channel){
                override fun handleDelivery(
                    consumerTag: String?,
                    envelope: Envelope?,
                    properties: AMQP.BasicProperties?,
                    body: ByteArray?
                ) {
                    super.handleDelivery(consumerTag, envelope, properties, body)
                    val message = body?.let { String(it, charset("UTF-8")) }

                    val intent = Intent(this@MyService, MyService::class.java)
                    intent.putExtra("message","")
                    startService(intent)
                }
            }
            channel.basicConsume(QUE_NAME, true, consumer)
        }catch (e:Exception){
            Log.i(TAG,"Error: "+e.javaClass.name)
        }

    }

}