package com.mobdeve.s11.group2.moneymonster.history

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.RelativeDateTimeFormatter.RelativeDateTimeUnit
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.group2.moneymonster.MonthYearPickerDialog
import com.mobdeve.s11.group2.moneymonster.finance.FinanceDatabaseHelper
import com.mobdeve.s11.group2.moneymonster.R
import com.mobdeve.s11.group2.moneymonster.SettingsActivity
import com.mobdeve.s11.group2.moneymonster.databinding.HistoryBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import kotlin.time.Duration.Companion.days

class HistoryActivity : ComponentActivity() {

    private lateinit var dateRangeSpinner2: Spinner
    private lateinit var timePeriodTv: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: FinanceDatabaseHelper
    private lateinit var timePeriod: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding: HistoryBinding = HistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        timePeriodTv = viewBinding.TimePeriodTv
        timePeriodTv.setOnClickListener() {
            val monthYearPickerDialog = MonthYearPickerDialog(
                this,
                {month, year -> val formattedDate = String.format(
                    "%02d-%02d-%04d", 1, month, year)
                }
            )
            monthYearPickerDialog.show()


        }
//        dateRangeSpinner2 = findViewById(R.id.dateRangeSpnr2)

        val sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        timePeriod = sharedPref.getString(SettingsActivity.TIME, "Weekly") ?: "Weekly"

//        val historyPeriods = generateWeekRanges()
//        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, historyPeriods)
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        dateRangeSpinner2.adapter = spinnerAdapter

        databaseHelper = FinanceDatabaseHelper(this)

        recyclerView = findViewById(R.id.historyRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val records = databaseHelper.getAllRecords()

        val groupedByDateAndType = records
            .groupBy { it.date }
            .toSortedMap(compareByDescending { it })
            .mapValues { entry ->
                entry.value.groupBy { it.type }
            }

        val adapter = HistoryRecordDateAdapter(groupedByDateAndType)
        recyclerView.adapter = adapter
    }

    private fun generateWeekRanges(): ArrayList<String>{
        var date = Calendar.getInstance()
        var timePeriodList = ArrayList<String>()

        if (timePeriod == "Daily") {
            date.add(Calendar.DAY_OF_MONTH, -4)
            val formatter = SimpleDateFormat("MMM dd, yyyy")
            for (i in 1..7) {
                date.add(Calendar.DAY_OF_MONTH,1)
                timePeriodList.add(formatter.format(date.time))
            }
        } else if (timePeriod == "Monthly") {
            date.add(Calendar.MONTH, -4)
            val formatter = SimpleDateFormat("MMMM, yyyy")
            for (i in 1..5){
                date.add(Calendar.MONTH, 1)
                timePeriodList.add(formatter.format(date.time))
            }
        }
//        return listOf(
//            "October 1 - October 7",
//            "October 8 - October 14",
//            "October 15 - October 21",
//            "October 22 - October 28",
//            "October 29 - November 4"
//        )
        return timePeriodList
    }
}