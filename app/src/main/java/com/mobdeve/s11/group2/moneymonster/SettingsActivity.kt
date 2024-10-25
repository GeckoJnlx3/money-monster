package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.mobdeve.s11.group2.moneymonster.databinding.SettingsBinding

class SettingsActivity : ComponentActivity() {

    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currencySpinner: Spinner = binding.currencySpnr
        val timeSpinner: Spinner = binding.timeSpnr

        val currencies = arrayOf("USD", "EUR", "JPY", "GBP", "AUD")
        val currencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = currencyAdapter

        val timeOptions = arrayOf("Daily", "Weekly", "Monthly", "Yearly")
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeOptions)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}