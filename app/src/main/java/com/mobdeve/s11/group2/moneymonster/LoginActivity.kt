package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.LoginBinding

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding: LoginBinding = LoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        var registerTv: TextView = viewBinding.loginRegisterNowTv
        registerTv.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        var enterBtn: TextView = viewBinding.loginEnterBtn
        enterBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
