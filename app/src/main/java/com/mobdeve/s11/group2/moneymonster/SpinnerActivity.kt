package com.mobdeve.s11.group2.moneymonster

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mobdeve.s11.group2.moneymonster.databinding.RegisterBinding
import java.util.Locale
import android.widget.Spinner

class SpinnerActivity : ComponentActivity() {

    private lateinit var dateRangeSpnr: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val weekRanges = generateWeekRanges()

        val weekRangeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weekRanges)
        weekRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateRangeSpnr.adapter = weekRangeAdapter
    }

    private fun generateWeekRanges(): List<String> {
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.MONTH, Calendar.OCTOBER)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val weeks = mutableListOf<String>()

        repeat(5) {
            val startOfWeek = calendar.time
            calendar.add(Calendar.DAY_OF_MONTH, 6)
            val endOfWeek = calendar.time

            val range = "${dateFormat.format(startOfWeek)} - ${dateFormat.format(endOfWeek)}"
            weeks.add(range)

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weeks
    }
}