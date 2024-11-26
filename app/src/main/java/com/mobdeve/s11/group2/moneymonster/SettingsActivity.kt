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

    companion object {
        const val CURRENCY = "CURRENCY"
        const val TARGET = "TARGET"
        const val LIMIT = "LIMIT"
        const val TIME = "TIME"

        val CURRENCY_LIST = arrayOf("PHP", "USD", "EUR", "JPY", "GBP", "AUD")
        val TIME_OPTIONS = arrayOf("Daily", "Monthly", "Yearly")

        const val PREFERENCE_FILE = "com.mobdeve.s11.group2.moneymonster.PREFERENCE_FILE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currencySpinner: Spinner = binding.currencySpnr
        val currencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, CURRENCY_LIST)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = currencyAdapter

        val sharedPref = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
        val savedCurrency = sharedPref.getString(CURRENCY, "PHP")
        val selectedCurrencyPosition = CURRENCY_LIST.indexOf(savedCurrency)
        if (selectedCurrencyPosition >= 0) {
            currencySpinner.setSelection(selectedCurrencyPosition)
        }

        val timeSpinner: Spinner = binding.timeSpnr
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, TIME_OPTIONS)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter

        val savedTime = sharedPref.getString(TIME, "Daily")
        val selectedTimePosition = TIME_OPTIONS.indexOf(savedTime)
        if (selectedTimePosition >= 0) {
            timeSpinner.setSelection(selectedTimePosition)
        }

        binding.saveTargetBtn.setOnClickListener {
            val target = binding.setTarget.text.toString().toDoubleOrNull()
            if (target != null) {
                saveToPreferences(TARGET, target)
                Toast.makeText(this, "Target set to $target", Toast.LENGTH_SHORT).show()
                binding.setTarget.text.clear()
            } else {
                Toast.makeText(this, "Please enter a valid target (e.g., 500.00)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.expenseLimitBtn.setOnClickListener {
            val limit = binding.setLimit.text.toString().toDoubleOrNull()
            if (limit != null) {
                saveToPreferences(LIMIT, limit)
                Toast.makeText(this, "Limit set to $limit", Toast.LENGTH_SHORT).show()
                binding.setLimit.text.clear()
            } else {
                Toast.makeText(this, "Please enter a valid limit (e.g., 300.00)", Toast.LENGTH_SHORT).show()
            }
        }

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCurrency = parent?.getItemAtPosition(position).toString()
                saveToPreferences(CURRENCY, selectedCurrency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTime = parent?.getItemAtPosition(position).toString()
                saveToPreferences(TIME, selectedTime)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveToPreferences(key: String, value: Double) {
        val sharedPref = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat(key, value.toFloat()) // Store double as float due to SharedPreferences limitation
            apply()
        }
    }

    private fun saveToPreferences(key: String, value: String) {
        val sharedPref = getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }
}
