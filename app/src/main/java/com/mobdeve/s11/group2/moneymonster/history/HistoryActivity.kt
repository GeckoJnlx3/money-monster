package com.mobdeve.s11.group2.moneymonster.history

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.DatabaseHelper
import com.mobdeve.s11.group2.moneymonster.history.dialog.MonthYearPickerDialog
import com.mobdeve.s11.group2.moneymonster.SettingsActivity
import com.mobdeve.s11.group2.moneymonster.SettingsActivity.Companion.TIME
import com.mobdeve.s11.group2.moneymonster.TimePeriodUtils
import com.mobdeve.s11.group2.moneymonster.history.dialog.YearPickerDIalog
import com.mobdeve.s11.group2.moneymonster.databinding.HistoryBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryActivity : ComponentActivity() {

    private lateinit var timePeriodSpnr: Spinner
    private lateinit var timePeriodBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var timePeriod: String
    private var selectedMonth: Int? = null
    private var selectedYear: Int? = null


  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding
        var viewBinding: HistoryBinding = HistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        timePeriodBtn = viewBinding.timePeriodBtn
        timePeriodSpnr = viewBinding.timePeriodSpinner
        recyclerView = viewBinding.historyRecycler

        // setting time period preference
        var sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        timePeriod = sharedPref.getString(SettingsActivity.TIME, "Monthly") ?: "Monthly"

        // setting up default values and onClickListeners
        setTimePeriodTv()
        setTimePeriodSpinner(sharedPref)

        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = DatabaseHelper(this)
        updateRv()
    }

    private fun setTimePeriodSpinner(sharedPref: SharedPreferences){
        // setting up spinner for time period
        val spinnerAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            TimePeriodUtils.TIME_PERIOD_LIST)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timePeriodSpnr.adapter = spinnerAdapter

        // setting up shared preference
        val savedTime = sharedPref.getString(TIME, "Daily")
        val selectedTimePosition = SettingsActivity.TIME_OPTIONS.indexOf(savedTime)
        if (selectedTimePosition >= 0) {
            timePeriodSpnr.setSelection(selectedTimePosition)
        }

        // change UI on click
        timePeriodSpnr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTime = parent?.getItemAtPosition(position).toString()
                val sp = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
                with(sp.edit()) {
                    putString(TIME, selectedTime)
                    apply()
                }
                timePeriod = timePeriodSpnr.selectedItem.toString()
                timePeriodBtn.text = setDefaultTimePeriod()

                updateRv()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setTimePeriodTv(){
        // displaying the correct format depending on time period preference
        timePeriodBtn.text = setDefaultTimePeriod()

        // listener that displays dialog depending on time period value
        timePeriodBtn.setOnClickListener() {
            if (timePeriod == "Monthly"){
                val monthYearPickerDialog = MonthYearPickerDialog(
                    this,
                    {month, year ->
                        val formattedDate = String.format(Locale.getDefault(),
                            "%02d-%04d", month, year)
                        selectedMonth = month
                        selectedYear = year
                        timePeriodBtn.text = formattedDate
                        updateRv()
                    }

                )
                monthYearPickerDialog.show()
            } else if (timePeriod == "Yearly"){
                val yearPickerDialog = YearPickerDIalog(
                    this,
                    {year ->
                        val formattedDate = String.format(Locale.getDefault(),
                            "%04d", year)
                        selectedMonth = null
                        selectedYear = year
                        timePeriodBtn.text = formattedDate
                        updateRv()
                    })
                yearPickerDialog.show()
            } else {
                selectedMonth = null
                selectedYear = null
                updateRv()
            }
            Log.d("time period on click", selectedMonth.toString() + selectedYear.toString())

        }
    }

    private fun setDefaultTimePeriod(): String {
        val dateToday = Calendar.getInstance().time
        when (timePeriod){
            "Daily" -> {
                selectedYear = null
                selectedMonth = null
                return "Latest logs"
            }
            "Monthly" -> {
                selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
                selectedYear = Calendar.getInstance().get(Calendar.YEAR)
                return TimePeriodUtils.MONTH_YEAR_FORMATTER.format(dateToday)
            }
            "Yearly" -> {
                selectedMonth = null
                selectedYear = Calendar.getInstance().get(Calendar.YEAR)
                return TimePeriodUtils.YEAR_FORMATTER.format(dateToday)
            }
            else -> return "Invalid time period"
        }
    }

    private fun updateRv(){
        val records = databaseHelper.getAllRecords(selectedMonth, selectedYear)

        val groupedByDateAndType = records
            .groupBy { it.date }
            .toSortedMap(compareByDescending { it })
            .mapValues { entry ->
                entry.value.groupBy { it.type }
            }

        val adapter = HistoryRecordDateAdapter(groupedByDateAndType, this)
        recyclerView.adapter = adapter
    }


}