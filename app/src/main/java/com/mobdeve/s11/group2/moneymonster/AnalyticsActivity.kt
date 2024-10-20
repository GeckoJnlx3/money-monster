package com.mobdeve.s11.group2.moneymonster

import android.R
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Typeface
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.mobdeve.s11.group2.moneymonster.databinding.AnalyticsBinding

class AnalyticsActivity: ComponentActivity() {
    lateinit var expensePc: PieChart
    private var colors: ArrayList<Int> = ArrayList()

    init{
        colors.add(resources.getColor(R.color.holo_purple))
        colors.add(resources.getColor(R.color.holo_green_dark))
        colors.add(resources.getColor(R.color.holo_red_light))
        colors.add(resources.getColor(R.color.holo_orange_light))
        colors.add(resources.getColor(R.color.holo_blue_light))
    }

    // TODO: create a line graph for expenses/savings through time
    // TODO: the other thing i forgot what it was!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding: AnalyticsBinding = AnalyticsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        expensePc = viewBinding.expensePc

        displayExpensePieChart()

    }

    fun displayExpensePieChart(){

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
        expensePds.setColors(colors)

        val data = PieData(expensePds)
        expensePc.setData(data)

        expensePc.highlightValues(null)
        expensePc.invalidate()
    }

}