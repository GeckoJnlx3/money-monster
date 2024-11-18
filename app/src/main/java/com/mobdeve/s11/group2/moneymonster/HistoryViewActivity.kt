package com.mobdeve.s11.group2.moneymonster.com.mobdeve.s11.group2.moneymonster

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.R

class HistoryViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_record_view)

        val date = intent.getStringExtra("record_date") ?: ""
        val currency = intent.getStringExtra("record_currency") ?: ""
        val amount = intent.getStringExtra("record_amount") ?: "0.0"
        val category = intent.getStringExtra("record_category") ?: ""
        val description = intent.getStringExtra("record_description") ?: ""

        val dateTextView: TextView = findViewById(R.id.date)
        val amountTextView: TextView = findViewById(R.id.amount)
        val categoryTextView: TextView = findViewById(R.id.category)
        val descriptionTextView: TextView = findViewById(R.id.description)

        dateTextView.text = date
        amountTextView.text = "$currency " + "$amount"
        categoryTextView.text = category
        descriptionTextView.text = description

    }
}
