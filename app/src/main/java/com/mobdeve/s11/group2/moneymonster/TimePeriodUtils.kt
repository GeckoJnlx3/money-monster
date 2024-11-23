package com.mobdeve.s11.group2.moneymonster

import com.mobdeve.s11.group2.moneymonster.history.HistoryActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimePeriodUtils {
    val DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd", Locale("en-PH(*)"))
    val MONTH_YEAR_FORMATTER =  SimpleDateFormat("MM yyyy", Locale("en-PH(*)"))
    val YEAR_FORMATTER = SimpleDateFormat("yyyy", Locale("en-PH(*)"))

    val TIME_PERIOD_LIST = listOf<String>("Daily", "Monthly", "Yearly")
}