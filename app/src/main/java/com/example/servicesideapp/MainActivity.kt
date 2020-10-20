package com.example.servicesideapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ServiceDemo"
    }

    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(applicationContext, MyService::class.java)
        startServiceBtn.setOnClickListener {
            Log.i(TAG, "Service started on thread " + Thread.currentThread().id)
            startService(serviceIntent)
        }

        stopServiceBtn.setOnClickListener {
            stopService(serviceIntent)
        }
    }
}