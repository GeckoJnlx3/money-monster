package com.mobdeve.s11.group2.moneymonster.history.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Button
import com.mobdeve.s11.group2.moneymonster.R
import java.util.Calendar

class MonthYearPickerDialog(context: Context, private val onDateSet: (Int, Int) -> Unit) : Dialog(context) {
    private lateinit var monthPicker: NumberPicker
    private lateinit var yearPicker: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_month_year_picker)

        monthPicker = findViewById(R.id.month_picker)
        yearPicker = findViewById(R.id.year_picker)
        val okButton = findViewById<Button>(R.id.ok_button)

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        // Set up the month picker (0-11)
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = currentMonth

        // Set up the year picker
        yearPicker.minValue = currentYear - 100
        yearPicker.maxValue = currentYear + 100
        yearPicker.value = currentYear

        okButton.setOnClickListener {
            val selectedMonth = monthPicker.value
            val selectedYear = yearPicker.value
            onDateSet(selectedMonth, selectedYear)
            dismiss()
        }
    }
}