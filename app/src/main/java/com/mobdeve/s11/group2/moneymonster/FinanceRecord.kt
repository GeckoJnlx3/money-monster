package com.mobdeve.s11.group2.moneymonster

import java.util.Date

data class FinanceRecord (
    val id: Int,
    val type: String,
    val category: String, // Int category ex. 1 Food 2 Utility (havent done)
    val amount: String?,
    val date: Date,
    val description: String,
)