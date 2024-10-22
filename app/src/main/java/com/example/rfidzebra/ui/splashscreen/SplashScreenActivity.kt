package com.example.rfidzebra.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.rfidzebra.MainActivity
import com.example.rfidzebra.R
import com.example.rfidzebra.ui.feature.ConnectActivity
import com.example.rfidzebra.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashTime: Long = 2000

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,  MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTime)
    }
}