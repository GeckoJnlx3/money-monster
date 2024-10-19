package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.RegisterBinding

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding: RegisterBinding = RegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        var loginBtn: TextView = viewBinding.registerLogInTv
        loginBtn.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        var enterBtn: Button = viewBinding.registerBtn
        enterBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
