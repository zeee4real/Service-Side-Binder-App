package com.example.servicesideapp


import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import com.example.servicesideapp.MainActivity.Companion.TAG
import java.util.*

class MyService : Service() {

    private var randomNumber: Int = 0
    private var isRandomNumberGeneratorOn: Boolean = false

    private val min = 0
    private val max = 100
    companion object {
        private const val GET_RANDOM_NUMBER = 0

    }

    inner class RandomNumberRequestHandler: Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                GET_RANDOM_NUMBER -> {
                    val messageSendRandomNumber = Message.obtain(null, GET_RANDOM_NUMBER)
                    messageSendRandomNumber.arg1 = getRandomNumber()
                    try {
                        msg.replyTo.send(messageSendRandomNumber)
                    } catch (e: RemoteException) {
                        Log.i(TAG,""+e.message)
                    }
                }
            }
            super.handleMessage(msg)
        }
    }

    private val randomNumberMessenger: Messenger = Messenger(RandomNumberRequestHandler())


    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "In onBind " + Thread.currentThread().id)
        return if(intent?.`package`.equals("com.example.clientsidebinderapp")) {
            Toast.makeText(applicationContext,"Correct Package", Toast.LENGTH_SHORT).show()
            randomNumberMessenger.binder
        } else {
            Toast.makeText(applicationContext,"Wrong Package", Toast.LENGTH_SHORT).show()
            null
        }
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "In onStartCommand Thread id " + Thread.currentThread().id)
        isRandomNumberGeneratorOn = true
        Thread {
            startRandomNumberGenerator()
        }.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
        Log.i(TAG, "Service Destroyed")
    }

    private fun startRandomNumberGenerator() {
        while (isRandomNumberGeneratorOn) {
            try {
                Thread.sleep(1000)
                if (isRandomNumberGeneratorOn) {
                    randomNumber = Random().nextInt(max) + min;
                    Log.i(
                        TAG,
                        "Thread id: " + Thread.currentThread().id + ", Random Number: " + randomNumber
                    )
                }
            } catch (i: InterruptedException) {
                Log.i(TAG, "Thread Interrupted")
            }
        }
    }

    private fun stopRandomNumberGenerator() {
        isRandomNumberGeneratorOn = false
    }

    fun getRandomNumber(): Int = randomNumber

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "In onUnbind")
        return super.onUnbind(intent)
    }



}