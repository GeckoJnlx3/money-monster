package com.mobdeve.s11.group2.moneymonster

import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.PieDataSet
import com.mobdeve.s11.group2.moneymonster.databinding.AnalyticsBinding

class AnalyticsActivity: ComponentActivity() {
    lateinit var expensePc: PieChart
    // TODO: create a line graph for expenses/savings through time
    // TODO: the other thing i forgot what it was!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewBinding: AnalyticsBinding = AnalyticsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        expensePc = viewBinding.expensePc
        var expenseList:ArrayList<PieEntry> = ArrayList()
        expenseList.add(PieEntry(70f, "Food"))
        expenseList.add(PieEntry(30f, "Transport"))

        var expensePds = PieDataSet(expenseList, "Expenses per category")
        expensePds.sliceSpace = 3f

        // TODO: connect dataset to the pie chart

        // TODO: display the pie chart



    }
}