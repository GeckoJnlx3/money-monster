package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.register)

        var loginBtn: TextView = findViewById(R.id.registerLogInTv)
        loginBtn.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
