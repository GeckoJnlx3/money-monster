package com.mobdeve.s11.group2.moneymonster

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.mobdeve.s11.group2.moneymonster.databinding.AnalyticsBinding
import com.mobdeve.s11.group2.moneymonster.finance.FinanceDatabaseHelper

class AnalyticsActivity : ComponentActivity() {

    lateinit var expensePc: PieChart
    private lateinit var dateRangeSpinner: Spinner
    private var colors: ArrayList<Int> = ArrayList()
    private lateinit var databaseHelper: FinanceDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: AnalyticsBinding = AnalyticsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        databaseHelper = FinanceDatabaseHelper(this)

        dateRangeSpinner = viewBinding.dateRangeSpnr
        val weekRanges = generateWeekRanges()
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, weekRanges)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        dateRangeSpinner.adapter = adapter

        colors.add(ContextCompat.getColor(this, R.color.holo_purple))
        colors.add(ContextCompat.getColor(this, R.color.holo_green_dark))
        colors.add(ContextCompat.getColor(this, R.color.holo_red_light))
        colors.add(ContextCompat.getColor(this, R.color.holo_orange_light))
        colors.add(ContextCompat.getColor(this, R.color.holo_blue_light))

        expensePc = viewBinding.expensePc
        displayExpensePieChart()
    }

    private fun generateWeekRanges(): List<String> {
        return listOf(
            "October 1 - October 7",
            "October 8 - October 14",
            "October 15 - October 21",
            "October 22 - October 28",
            "October 29 - November 4"
        )
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
                categoryExpenseMap[category] = categoryExpenseMap.getOrDefault(category, 0f) + amount
            }
        }

        val expenseEntries = ArrayList<PieEntry>()
        categoryExpenseMap.forEach { (category, totalAmount) ->
            expenseEntries.add(PieEntry(totalAmount, category))
        }

        val expensePds = PieDataSet(expenseEntries, "Expense Categories")
        expensePds.sliceSpace = 3f
        expensePds.colors = colors

        val data = PieData(expensePds)
        expensePc.setData(data)
        expensePc.invalidate()
    }
}
//    private fun displayOverviewLineChart() {
//        overviewLc.xAxis.valueFormatter = LineChartXAxisValueFormatter()
//        overviewLc.description.isEnabled = false
//
//        var expenseOverview: ArrayList<Entry> = ArrayList()
//        expenseOverview.add(Entry(1725120000000f, 0f))
//        expenseOverview.add(Entry(1725206400000f, 100f))
//        expenseOverview.add(Entry(1726006400000f, 3000f))
//        expenseOverview.add(Entry(1726806400000f, 5000f))
//
//        val expenseOverviewDs = LineDataSet(expenseOverview, "Expenses")
//        expenseOverviewDs.color = ContextCompat.getColor(this, R.color.holo_purple)
//        expenseOverviewDs.lineWidth = 3f
//        expenseOverviewDs.setCircleColor(ContextCompat.getColor(this, R.color.black))
//        expenseOverviewDs.setDrawCircleHole(false)
//
//        var savingOverview: ArrayList<Entry> = ArrayList()
//        savingOverview.add(Entry(1725120000000f, 10000f))
//        savingOverview.add(Entry(1725206400000f, 9900f))
//        savingOverview.add(Entry(1726006400000f, 6900f))
//        savingOverview.add(Entry(1726806400000f, 1900f))
//
//        val savingOverviewDs = LineDataSet(savingOverview, "Savings")
//        savingOverviewDs.color = ContextCompat.getColor(this, R.color.holo_green_light)
//        savingOverviewDs.lineWidth = 3f
//        savingOverviewDs.setCircleColor(ContextCompat.getColor(this, R.color.black))
//        savingOverviewDs.setDrawCircleHole(false)
//
//        val data = LineData()
//        data.addDataSet(expenseOverviewDs)
//        data.addDataSet(savingOverviewDs)
//
//        overviewLc.data = data
//
//        overviewLc.invalidate()
//    }
//
//    private class LineChartXAxisValueFormatter: IndexAxisValueFormatter(){
//        override fun getFormattedValue(value: Float): String? {
//            //https://stackoverflow.com/questions/41426021/how-to-add-x-axis-as-datetime-label-in-mpandroidchart
//            val msSince1970 = TimeUnit.DAYS.toMillis(value.toLong())
//            val timeMs: Date = Date(msSince1970)
//            val loc: Locale = Locale("en")
//            val dateTimeFormat:SimpleDateFormat = SimpleDateFormat("MM-dd-yy", loc)
////            val format: SimpleDateFormat = SimpleDateFormat("MM-dd")
//
//            return dateTimeFormat.format(timeMs)
//        }
//    }
//}