package com.mobdeve.s11.group2.moneymonster

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.SettingsBinding
import com.mobdeve.s11.group2.moneymonster.login.LoginActivity

class SettingsActivity : ComponentActivity() {

    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currencySpinner: Spinner = binding.currencySpnr
        val currencies = arrayOf("PHP", "USD", "EUR", "JPY", "GBP", "AUD")
        val currencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = currencyAdapter

        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val savedCurrency = sharedPref.getString("CURRENCY", "PHP")
        val selectedCurrencyPosition = currencies.indexOf(savedCurrency)
        if (selectedCurrencyPosition >= 0) {
            currencySpinner.setSelection(selectedCurrencyPosition)
        }

        val timeSpinner: Spinner = binding.timeSpnr
        val timeOptions = arrayOf("Daily", "Weekly", "Monthly", "Yearly")
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeOptions)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter

        val savedTime = sharedPref.getString("TIME", "Daily")
        val selectedTimePosition = timeOptions.indexOf(savedTime)
        if (selectedTimePosition >= 0) {
            timeSpinner.setSelection(selectedTimePosition)
        }

        binding.saveTargetBtn.setOnClickListener {
            val target = binding.setTarget.text.toString().toIntOrNull()
            if (target != null) {
                saveToPreferences("TARGET", target)
                Toast.makeText(this, "Target set", Toast.LENGTH_SHORT).show()
                binding.setTarget.text.clear()
            } else {
                Toast.makeText(this, "Please enter a target", Toast.LENGTH_SHORT).show()
            }
        }

        binding.expenseLimitBtn.setOnClickListener {
            val limit = binding.setLimit.text.toString().toIntOrNull()
            if (limit != null) {
                saveToPreferences("LIMIT", limit)
                Toast.makeText(this, "Limit set", Toast.LENGTH_SHORT).show()
                binding.setLimit.text.clear()
            } else {
                Toast.makeText(this, "Please enter a limit", Toast.LENGTH_SHORT).show()
            }
        }

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCurrency = parent?.getItemAtPosition(position).toString()
                saveToPreferences("CURRENCY", selectedCurrency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTime = parent?.getItemAtPosition(position).toString()
                saveToPreferences("TIME", selectedTime)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveToPreferences(key: String, value: Int) {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }

    private fun saveToPreferences(key: String, value: String) {
        val sharedPref = getSharedPreferences("com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }
}
