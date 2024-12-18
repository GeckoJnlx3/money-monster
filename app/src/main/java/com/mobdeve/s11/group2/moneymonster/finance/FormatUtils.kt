package com.mobdeve.s11.group2.moneymonster.finance

object FormatUtils {
    fun formatAmount(amount: Double, currency: String): String {
        return "$currency " + String.format("%.2f", amount)
    }
}
