package com.mobdeve.s11.group2.moneymonster

import android.R
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Typeface
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mobdeve.s11.group2.moneymonster.databinding.AnalyticsBinding
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AnalyticsActivity: ComponentActivity() {
    lateinit var expensePc: PieChart
    lateinit var overviewLc: LineChart
    private var colors: ArrayList<Int> = ArrayList()

    // TODO: create a line graph for expenses/savings through time
    // TODO: the other thing i forgot what it was!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding: AnalyticsBinding = AnalyticsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        colors.add(resources.getColor(R.color.holo_purple))
        colors.add(resources.getColor(R.color.holo_green_dark))
        colors.add(resources.getColor(R.color.holo_red_light))
        colors.add(resources.getColor(R.color.holo_orange_light))
        colors.add(resources.getColor(R.color.holo_blue_light))

        expensePc = viewBinding.expensePc
        displayExpensePieChart()

        overviewLc = viewBinding.overviewLc
        displayOverviewLineChart()

    }

    private fun displayExpensePieChart(){

        expensePc.extraRightOffset = 30f
        expensePc.isDrawHoleEnabled = false
        expensePc.setUsePercentValues(false)
        expensePc.setDrawEntryLabels(false)
        expensePc.description.isEnabled = false

        var expenseLegend:Legend = expensePc.legend
        expenseLegend.form = Legend.LegendForm.CIRCLE
        expenseLegend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        expenseLegend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        expenseLegend.orientation = Legend.LegendOrientation.VERTICAL

        // TODO: separate this into a parameter "data" so that it can change when needed
        var expenseList:ArrayList<PieEntry> = ArrayList()
        expenseList.add(PieEntry(70f, "Food"))
        expenseList.add(PieEntry(10f, "Transport"))
        expenseList.add(PieEntry(100f, "Meds"))

        var expensePds = PieDataSet(expenseList, "")
        expensePds.sliceSpace = 3f
        expensePds.colors = colors

        val data = PieData(expensePds)
        expensePc.setData(data)

        expensePc.highlightValues(null)
        expensePc.invalidate()
    }
    private fun displayOverviewLineChart() {
        var expenseOverview: ArrayList<Entry> = ArrayList()
        overviewLc.xAxis.valueFormatter = LineChartXAxisValueFormatter()

        expenseOverview.add(Entry(1725120000000f, 0f))
        expenseOverview.add(Entry(1725206400000f, 100f))
        expenseOverview.add(Entry(1726006400000f, 3000f))
        expenseOverview.add(Entry(1726806400000f, 5000f))

        val expenseOverviewDs = LineDataSet(expenseOverview, "")
        expenseOverviewDs.colors = colors

        val data = LineData(expenseOverviewDs)
        overviewLc.data = data

        overviewLc.highlightValues(null)
        overviewLc.invalidate()

    }

    private class LineChartXAxisValueFormatter: IndexAxisValueFormatter(){
        override fun getFormattedValue(value: Float): String? {
            //https://stackoverflow.com/questions/41426021/how-to-add-x-axis-as-datetime-label-in-mpandroidchart
            val msSince1970 = TimeUnit.DAYS.toMillis(value.toLong())
            val timeMs: Date = Date(msSince1970)
            val loc: Locale = Locale("en")
            val dateTimeFormat:SimpleDateFormat = SimpleDateFormat("MM-dd", loc)
//            val format: SimpleDateFormat = SimpleDateFormat("MM-dd")

            return dateTimeFormat.format(timeMs)
        }
    }
}