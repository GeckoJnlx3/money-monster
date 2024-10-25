package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mobdeve.s11.group2.moneymonster.databinding.SettingsBinding

class SettingsActivity : ComponentActivity() {

    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        loginBtn = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}