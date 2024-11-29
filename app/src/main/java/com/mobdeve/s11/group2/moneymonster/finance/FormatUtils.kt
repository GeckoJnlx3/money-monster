package com.mobdeve.s11.group2.moneymonster.finance
import java.text.NumberFormat
import java.util.*

object FormatUtils {
    fun formatAmount(amount: Double, currency: String): String {
        return "$currency " + String.format("%.2f", amount)
    }
    fun formatCurrency(currencyCode: String, amount: Double): String {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(currencyCode)
        return format.format(amount)
    }
}
