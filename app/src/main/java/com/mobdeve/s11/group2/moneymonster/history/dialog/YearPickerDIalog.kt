package com.mobdeve.s11.group2.moneymonster.history.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import com.mobdeve.s11.group2.moneymonster.R
import java.util.Calendar

class YearPickerDIalog (context: Context, private val onDateSet: (Int) -> Unit) : Dialog(context) {
    private lateinit var yearPicker: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_year_picker)

        yearPicker = findViewById(R.id.year_picker)
        val okButton = findViewById<Button>(R.id.ok_button)

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        // Set up the year picker
        yearPicker.minValue = currentYear - 100
        yearPicker.maxValue = currentYear + 100
        yearPicker.value = currentYear

        okButton.setOnClickListener {
            val selectedYear = yearPicker.value
            onDateSet(selectedYear)
            dismiss()
        }
    }

}