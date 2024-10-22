package com.example.rfidzebra.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rfidzebra.databinding.ActivityLoginBinding
import com.example.rfidzebra.ui.feature.ConnectActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for login button
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}