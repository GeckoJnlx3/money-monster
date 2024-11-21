package com.mobdeve.s11.group2.moneymonster

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.mobdeve.s11.group2.moneymonster.databinding.AnalyticsBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AnalyticsActivity : ComponentActivity() {

    private lateinit var expensePc: PieChart
    private lateinit var overviewLc: LineChart
    private lateinit var dateRangeSpinner: Spinner
    private var colors: ArrayList<Int> = ArrayList()
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: AnalyticsBinding = AnalyticsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        databaseHelper = DatabaseHelper(this)

        dateRangeSpinner = viewBinding.dateRangeSpnr
        expensePc = viewBinding.expensePc
        overviewLc = viewBinding.overviewLc

        setupDateRangeSpinner()
        setupColors()
        displayExpensePieChart()
        displayOverviewLineChart()
    }

    private fun setupDateRangeSpinner() {
        val weekRanges = generateDynamicWeekRanges()
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, weekRanges)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        dateRangeSpinner.adapter = adapter
    }

    private fun generateDynamicWeekRanges(): List<String> {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("MMM d", Locale("en"))
        val weekRanges = mutableListOf<String>()

        for (i in 0 until 5) {
            val endDate = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            val startDate = calendar.time
            weekRanges.add("${sdf.format(startDate)} - ${sdf.format(endDate)}")
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        return weekRanges
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

        val records = databaseHelper.getAllRecords()

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

        val records = databaseHelper.getAllRecords()
        val expenseOverview = ArrayList<Entry>()
        val savingOverview = ArrayList<Entry>()

        records.forEach { record ->
            val date = record.date.time.toFloat()
            val amount = record.amount?.toFloatOrNull() ?: 0f
            if (record.type == "Expense") {
                expenseOverview.add(Entry(date, amount))
            } else if (record.type == "Income") {
                savingOverview.add(Entry(date, amount))
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
        overviewLc.invalidate()
    }

    private class LineChartXAxisValueFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MMM d", Locale("en"))

        override fun getFormattedValue(value: Float): String {
            val date = Date(value.toLong())
            return dateFormat.format(date)
        }
    }
}
