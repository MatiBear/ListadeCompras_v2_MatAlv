package com.example.listadecompras_v2_matalv

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java) // Replace with your main menu activity
            startActivity(intent)
            finish()
        }, 2000) // Adjust the delay duration (in milliseconds) as needed
    }
}