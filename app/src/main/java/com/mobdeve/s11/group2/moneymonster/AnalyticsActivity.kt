package com.mobdeve.s11.group2.moneymonster

import android.R
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.mobdeve.s11.group2.moneymonster.SettingsActivity.Companion.TIME
import com.mobdeve.s11.group2.moneymonster.databinding.AnalyticsBinding
import com.mobdeve.s11.group2.moneymonster.finance.FinanceRecord
import com.mobdeve.s11.group2.moneymonster.history.dialog.MonthYearPickerDialog
import com.mobdeve.s11.group2.moneymonster.history.dialog.YearPickerDIalog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AnalyticsActivity : ComponentActivity() {

    private lateinit var expensePc: PieChart
    private lateinit var overviewLc: LineChart
    private lateinit var timePeriodBtn: Button
    private lateinit var timePeriodSpnr: Spinner

    private lateinit var timePeriod: String
    private var selectedMonth: Int? = null
    private var selectedYear: Int? = null
    private var selectedDay: Int? = null

    private var colors: ArrayList<Int> = ArrayList()
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: AnalyticsBinding = AnalyticsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        timePeriodSpnr = viewBinding.timePeriodSpinner
        timePeriodBtn = viewBinding.timePeriodBtn
        expensePc = viewBinding.expensePc
        overviewLc = viewBinding.overviewLc

        databaseHelper = DatabaseHelper(this)

        // setting time period preference
        var sharedPref = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
        timePeriod = sharedPref.getString(SettingsActivity.TIME, "Monthly") ?: "Monthly"

        // setting up functionality
        setTimePeriodSpinner(sharedPref)
        setTimePeriodTv()

        setupColors()
        displayExpensePieChart()
        displayOverviewLineChart()
    }

    private fun setTimePeriodSpinner(sharedPref: SharedPreferences) {
        // setting up spinner for time period
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            TimePeriodUtils.TIME_PERIOD_LIST
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timePeriodSpnr.adapter = spinnerAdapter

        // setting up shared preference
        val savedTime = sharedPref.getString(TIME, "Monthly")
        val selectedTimePosition = SettingsActivity.TIME_OPTIONS.indexOf(savedTime)
        if (selectedTimePosition >= 0) {
            timePeriodSpnr.setSelection(selectedTimePosition)
        }

        // change UI on click
        timePeriodSpnr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedTime = parent?.getItemAtPosition(position).toString()
                val sp = getSharedPreferences(SettingsActivity.PREFERENCE_FILE, MODE_PRIVATE)
                with(sp.edit()) {
                    putString(TIME, selectedTime)
                    apply()
                }
                timePeriod = timePeriodSpnr.selectedItem.toString()
                timePeriodBtn.text = setDefaultTimePeriod()

                displayExpensePieChart()
                displayOverviewLineChart()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setTimePeriodTv() {
        // displaying the correct format depending on time period preference
        timePeriodBtn.text = setDefaultTimePeriod()

        // listener that displays dialog depending on time period value
        timePeriodBtn.setOnClickListener() {
            if (timePeriod == "Monthly") {
                val monthYearPickerDialog = MonthYearPickerDialog(
                    this,
                    { month, year ->
                        val formattedDate = String.format(
                            Locale.getDefault(),
                            "%02d-%04d", month, year
                        )
                        selectedMonth = month
                        selectedYear = year
                        timePeriodBtn.text = formattedDate

                        displayExpensePieChart()
                        displayOverviewLineChart()
                    }

                )
                monthYearPickerDialog.show()
            } else if (timePeriod == "Yearly") {
                val yearPickerDialog = YearPickerDIalog(
                    this,
                    { year ->
                        val formattedDate = String.format(
                            Locale.getDefault(),
                            "%04d", year
                        )
                        selectedMonth = null
                        selectedYear = year
                        timePeriodBtn.text = formattedDate
                        displayExpensePieChart()
                        displayOverviewLineChart()
                    })
                yearPickerDialog.show()
            } else {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this, com.mobdeve.s11.group2.moneymonster.R.style.DatePickerDialogStyle,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val formattedDate = String.format(
                            Locale.getDefault(),
                            "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay
                        )
                        this.selectedYear = selectedYear
                        this.selectedMonth = selectedMonth + 1
                        this.selectedDay = selectedDay
                        timePeriodBtn.text = formattedDate
                        displayExpensePieChart()
                        displayOverviewLineChart()
                    },
                    year, month, day
                )
                datePickerDialog.show()

            }
            Log.d("time period on click", selectedMonth.toString() + selectedYear.toString())

        }
    }

    private fun setDefaultTimePeriod(): String {
        val dateToday = Calendar.getInstance().time
        when (timePeriod) {
            "Daily", "Monthly" -> {
                timePeriod = "Monthly"
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


    private fun setupColors() {
        colors.add(ContextCompat.getColor(this, R.color.holo_purple))
        colors.add(ContextCompat.getColor(this, R.color.holo_green_dark))
        colors.add(ContextCompat.getColor(this, R.color.holo_red_light))
        colors.add(ContextCompat.getColor(this, R.color.holo_orange_light))
        colors.add(ContextCompat.getColor(this, R.color.holo_blue_light))
    }

    private fun displayExpensePieChart() {
        expensePc.extraRightOffset = 30f
        expensePc.isDrawHoleEnabled = false
        expensePc.setUsePercentValues(false)
        expensePc.setDrawEntryLabels(false)
        expensePc.description.isEnabled = false

        val expenseLegend = expensePc.legend
        expenseLegend.form = Legend.LegendForm.CIRCLE
        expenseLegend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        expenseLegend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        expenseLegend.orientation = Legend.LegendOrientation.VERTICAL

        val records = databaseHelper.getAllRecords(selectedMonth, selectedYear)

        val categoryExpenseMap = mutableMapOf<String, Float>()
        records.forEach { record ->
            if (record.type == "Expense") {
                val category = record.category
                val amount = record.amount?.toFloatOrNull() ?: 0f
                categoryExpenseMap[category] =
                    categoryExpenseMap.getOrDefault(category, 0f) + amount
            }
        }

        if (categoryExpenseMap.isEmpty()) {
            expensePc.clear()
            expensePc.centerText = "No expense data available"
            return
        }

        val expenseEntries = ArrayList<PieEntry>()
        categoryExpenseMap.forEach { (category, totalAmount) ->
            expenseEntries.add(PieEntry(totalAmount, category))
        }

        val expensePds = PieDataSet(expenseEntries, "Categories")
        expensePds.sliceSpace = 3f
        expensePds.colors = colors

        val data = PieData(expensePds)
        expensePc.data = data
        expensePc.invalidate()
    }

    private fun displayOverviewLineChart() {
        overviewLc.xAxis.valueFormatter = LineChartXAxisValueFormatter()
        overviewLc.description.isEnabled = false

        val records = databaseHelper.getAllRecords(selectedMonth, selectedYear)

        val expenseOverview = ArrayList<Entry>()
        val savingOverview = ArrayList<Entry>()

        var totalExpense = 0.0
        var totalIncome = 0.0

        records.forEach { record ->
            val date = record.date.time.toFloat()
            val amount = record.amount?.toFloatOrNull() ?: 0f
            if (record.type == "Expense") {
                totalExpense += amount
                expenseOverview.add(Entry(date, totalExpense.toFloat()))
            } else if (record.type == "Income") {
                totalIncome += amount
                savingOverview.add(Entry(date, totalIncome.toFloat()))
            }
        }

        val expenseDataSet = LineDataSet(expenseOverview, "Expenses").apply {
            color = ContextCompat.getColor(this@AnalyticsActivity, R.color.holo_red_light)
            lineWidth = 3f
            setCircleColor(ContextCompat.getColor(this@AnalyticsActivity, R.color.black))
            setDrawCircleHole(false)
        }

        val savingDataSet = LineDataSet(savingOverview, "Income").apply {
            color = ContextCompat.getColor(this@AnalyticsActivity, R.color.holo_green_light)
            lineWidth = 3f
            setCircleColor(ContextCompat.getColor(this@AnalyticsActivity, R.color.black))
            setDrawCircleHole(false)
        }

        val lineData = LineData(expenseDataSet, savingDataSet)
        overviewLc.data = lineData

        overviewLc.axisLeft.apply {
            axisMinimum = 0f
            isGranularityEnabled = true
            granularity = 1f
        }

        overviewLc.axisRight.isEnabled = false

        overviewLc.invalidate()
    }

    private class LineChartXAxisValueFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MMM dd", Locale("en"))

        override fun getFormattedValue(value: Float): String {
            val date = Date(value.toLong())
            return dateFormat.format(date)
        }
    }
}

