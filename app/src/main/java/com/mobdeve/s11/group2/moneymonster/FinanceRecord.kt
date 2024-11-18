package com.mobdeve.s11.group2.moneymonster

import java.util.Date

data class FinanceRecord (
    val id: Int,
    val type: String,
    val date: Date,
    val currency: String,
    val amount: String?,
    val category: String,
    val description: String,
)