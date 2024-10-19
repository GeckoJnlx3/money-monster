package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mobdeve.s11.group2.moneymonster.databinding.ActivityMainBinding
import com.mobdeve.s11.group2.moneymonster.ui.theme.MoneyMonsterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val monsterpediaBtn: Button = viewBinding.monsterpediaBtn
        val loginBtn: Button = viewBinding.loginBtn
        val analyticsBtn: Button = viewBinding.analyticsBtn

        monsterpediaBtn.setOnClickListener(){
            val intent = Intent(this, MonsterActivity::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        // added soon
//        analyticsBtn.setOnClickListener(){
//            val intent = Intent(this, AnalyticsActivity::class.java)
//            startActivity(intent)
//        }

    }

}
